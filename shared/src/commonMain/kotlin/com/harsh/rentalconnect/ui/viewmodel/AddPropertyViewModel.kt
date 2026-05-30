package com.harsh.rentalconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.rentalconnect.domain.model.AddPropertyDraft
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.domain.model.PickedPhoto
import com.harsh.rentalconnect.domain.repository.PropertyRepository
import com.harsh.rentalconnect.domain.usecase.AddPropertyUseCase
import com.harsh.rentalconnect.domain.usecase.UpdatePropertyUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddPropertyUiState(
    val propertyName: String = "",
    val houseNumber: String = "",
    val fullAddress: String = "",
    val type: String = "",
    val photoUrlInput: String = "",
    val photoUrls: List<String> = emptyList(),
    val isUploadingPhotos: Boolean = false,
    val photoUploadError: String? = null,
    val loadError: String? = null,
    val isOccupied: Boolean = true,
    val isSaving: Boolean = false,
    val propertyNameError: String? = null,
    val houseNumberError: String? = null,
    val fullAddressError: String? = null,
    val typeError: String? = null,
)

class AddPropertyViewModel(
    private val propertyRepository: PropertyRepository,
    private val addProperty: AddPropertyUseCase,
    private val updateProperty: UpdatePropertyUseCase,
    private val currentUser: AuthUser,
    private val propertyId: String? = null,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPropertyUiState())
    val uiState: StateFlow<AddPropertyUiState> = _uiState.asStateFlow()

    init {
        if (propertyId != null) {
            viewModelScope.launch {
                runCatching {
                    propertyRepository.refreshPropertyDetail(propertyId)
                    propertyRepository.getPropertyById(propertyId).first()
                }.onSuccess { property ->
                    if (property == null) {
                        _uiState.update { it.copy(loadError = "We could not load this property.") }
                    } else {
                        _uiState.update {
                            it.copy(
                                propertyName = property.name,
                                houseNumber = property.houseNumber,
                                fullAddress = property.address,
                                type = property.type,
                                photoUrls = property.photoUrls,
                                isOccupied = property.isOccupied,
                                loadError = null,
                            )
                        }
                    }
                }.onFailure {
                    _uiState.update { state -> state.copy(loadError = "We could not load this property.") }
                }
            }
        }
    }

    fun onPropertyNameChange(value: String) = _uiState.update { it.copy(propertyName = value, propertyNameError = null) }
    fun onHouseNumberChange(value: String) = _uiState.update { it.copy(houseNumber = value, houseNumberError = null) }
    fun onFullAddressChange(value: String) = _uiState.update { it.copy(fullAddress = value, fullAddressError = null) }
    fun onTypeChange(value: String) = _uiState.update { it.copy(type = value, typeError = null) }
    fun onPhotoUrlInputChange(value: String) = _uiState.update { it.copy(photoUrlInput = value) }
    fun onAvailabilityChange(isOccupied: Boolean) = _uiState.update { it.copy(isOccupied = isOccupied) }
    fun onPhotoUploadErrorDismissed() = _uiState.update { it.copy(photoUploadError = null, loadError = null) }
    fun onPhotoUploadError(message: String) = _uiState.update { it.copy(photoUploadError = message) }

    fun addPhotoUrl() {
        val url = _uiState.value.photoUrlInput.trim()
        if (url.isBlank()) return
        _uiState.update {
            it.copy(
                photoUrls = (it.photoUrls + url).distinct(),
                photoUrlInput = "",
                photoUploadError = null,
            )
        }
    }

    fun removePhotoUrl(url: String) {
        _uiState.update { state -> state.copy(photoUrls = state.photoUrls.filterNot { it == url }) }
    }

    fun uploadPickedPhotos(photos: List<PickedPhoto>) {
        if (photos.isEmpty()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadingPhotos = true,
                    photoUploadError = null,
                )
            }

            val result = propertyRepository.uploadPropertyPhotos(
                ownerId = currentUser.id,
                photos = photos,
            )

            result.fold(
                onSuccess = { urls ->
                    _uiState.update {
                        it.copy(
                            photoUrls = (it.photoUrls + urls).distinct(),
                            isUploadingPhotos = false,
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isUploadingPhotos = false,
                            photoUploadError = error.message ?: "We could not upload the selected photos.",
                        )
                    }
                },
            )
        }
    }

    fun save(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.isUploadingPhotos) return
        val propertyNameError = state.propertyName.requiredError()
        val houseNumberError = state.houseNumber.requiredError()
        val fullAddressError = state.fullAddress.requiredError()
        val typeError = state.type.requiredError()
        val hasError = listOf(propertyNameError, houseNumberError, fullAddressError, typeError).any { it != null }

        _uiState.update {
            it.copy(
                propertyNameError = propertyNameError,
                houseNumberError = houseNumberError,
                fullAddressError = fullAddressError,
                typeError = typeError,
            )
        }
        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val draft = AddPropertyDraft(
                propertyName = state.propertyName,
                houseNumber = state.houseNumber,
                address = state.fullAddress,
                type = state.type,
                isOccupied = state.isOccupied,
                photoUrls = state.photoUrls,
            )
            if (propertyId == null) {
                addProperty(
                    draft = draft,
                    ownerId = currentUser.id,
                    ownerName = currentUser.name,
                    ownerPhone = currentUser.phone,
                )
            } else {
                updateProperty(propertyId, draft)
            }
            _uiState.update { it.copy(isSaving = false) }
            onSuccess()
        }
    }

    private fun String.requiredError(): String? = if (isBlank()) "This field is required" else null
}
