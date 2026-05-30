package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.harsh.rentalconnect.domain.model.AuthResult
import com.harsh.rentalconnect.domain.model.AuthValidationError
import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.ui.components.CountryCode
import com.harsh.rentalconnect.ui.components.PhoneInputField
import com.harsh.rentalconnect.ui.models.Role
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.Error
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OutlineSubtle
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.account_aadhar_label
import rentalconnect.shared.generated.resources.account_cancel
import rentalconnect.shared.generated.resources.account_edit
import rentalconnect.shared.generated.resources.account_email_label
import rentalconnect.shared.generated.resources.account_hometown_label
import rentalconnect.shared.generated.resources.account_name_label
import rentalconnect.shared.generated.resources.account_phone_label
import rentalconnect.shared.generated.resources.account_role_label
import rentalconnect.shared.generated.resources.account_role_owner
import rentalconnect.shared.generated.resources.account_role_tenant
import rentalconnect.shared.generated.resources.account_save
import rentalconnect.shared.generated.resources.account_sign_out
import rentalconnect.shared.generated.resources.account_title
import rentalconnect.shared.generated.resources.error_backend_unavailable
import rentalconnect.shared.generated.resources.error_generic_message
import rentalconnect.shared.generated.resources.error_generic_required
import rentalconnect.shared.generated.resources.error_name_empty
import rentalconnect.shared.generated.resources.error_name_invalid_format
import rentalconnect.shared.generated.resources.error_name_too_long
import rentalconnect.shared.generated.resources.error_name_too_short
import rentalconnect.shared.generated.resources.error_network_message
import rentalconnect.shared.generated.resources.error_phone_empty
import rentalconnect.shared.generated.resources.error_phone_invalid_format

@Composable
fun AccountScreen(
    userName: String,
    userEmail: String,
    userPhone: String,
    userHometown: String,
    userAadharId: String,
    role: Role,
    isEditing: Boolean,
    isSaving: Boolean,
    nameError: ValidationError? = null,
    phoneError: ValidationError? = null,
    hometownError: ValidationError? = null,
    aadharError: AuthValidationError? = null,
    saveError: AuthResult.Failure? = null,
    selectedCountryCode: CountryCode,
    onCountryCodeChange: (CountryCode) -> Unit,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onHometownChange: (String) -> Unit,
    onAadharChange: (String) -> Unit,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.account_title),
                        style = RentalConnectTheme.typography.titleLarge,
                    )
                },
                actions = {
                    if (isEditing) {
                        Row {
                            TextButton(onClick = onCancel) {
                                Text(stringResource(Res.string.account_cancel))
                            }
                            TextButton(onClick = onSave, enabled = !isSaving) {
                                Text(stringResource(Res.string.account_save))
                            }
                        }
                    } else {
                        TextButton(onClick = onEdit) {
                            Text(stringResource(Res.string.account_edit))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
            )
        },
        containerColor = Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
        ) {
            if (isEditing) {
                EditableField(stringResource(Res.string.account_name_label), userName, onNameChange, nameErrorMessage(nameError))
                PhoneInputField(
                    label = stringResource(Res.string.account_phone_label),
                    value = userPhone,
                    onValueChange = onPhoneChange,
                    selectedCountryCode = selectedCountryCode,
                    onCountryCodeChange = onCountryCodeChange,
                    isError = phoneError != null,
                    errorMessage = phoneErrorMessage(phoneError),
                    modifier = Modifier.fillMaxWidth(),
                )
                EditableField(stringResource(Res.string.account_hometown_label), userHometown, onHometownChange, hometownErrorMessage(hometownError))
                EditableField(stringResource(Res.string.account_aadhar_label), userAadharId, onAadharChange, aadharErrorMessage(aadharError), KeyboardType.Number)
                ReadOnlyCard(stringResource(Res.string.account_email_label), userEmail)
            } else {
                ReadOnlyCard(stringResource(Res.string.account_name_label), userName)
                ReadOnlyCard(stringResource(Res.string.account_email_label), userEmail)
                ReadOnlyCard(stringResource(Res.string.account_phone_label), userPhone)
                ReadOnlyCard(stringResource(Res.string.account_hometown_label), userHometown)
                ReadOnlyCard(stringResource(Res.string.account_aadhar_label), userAadharId)
            }

            saveError?.let {
                Text(
                    text = accountSaveErrorMessage(it),
                    style = RentalConnectTheme.typography.bodySmall,
                    color = Error,
                )
            }

            ReadOnlyCard(
                stringResource(Res.string.account_role_label),
                if (role == Role.Owner) stringResource(Res.string.account_role_owner) else stringResource(Res.string.account_role_tenant),
            )

            Button(
                onClick = onSignOut,
                shape = RoundedCornerShape(AppRadius.md),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppSpacing.md),
            ) {
                Text(
                    text = stringResource(Res.string.account_sign_out),
                    style = RentalConnectTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun ReadOnlyCard(label: String, value: String) {
    Card(
        shape = RoundedCornerShape(AppRadius.md),
        colors = CardDefaults.cardColors(containerColor = Surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xs),
        ) {
            Text(
                text = label,
                style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = OnSurfaceVariant,
            )
            Text(
                text = value,
                style = RentalConnectTheme.typography.bodyLarge,
                color = OnSurface,
            )
        }
    }
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
        Text(
            text = label,
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = errorMessage != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = KeyboardCapitalization.Words,
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                errorBorderColor = Error,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                errorContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = RentalConnectTheme.typography.bodySmall,
                color = Error,
            )
        }
    }
}

@Composable
private fun nameErrorMessage(error: ValidationError?): String? = when (error) {
    ValidationError.EMPTY -> stringResource(Res.string.error_name_empty)
    ValidationError.TOO_SHORT -> stringResource(Res.string.error_name_too_short)
    ValidationError.TOO_LONG -> stringResource(Res.string.error_name_too_long)
    ValidationError.INVALID_FORMAT -> stringResource(Res.string.error_name_invalid_format)
    null -> null
}

@Composable
private fun phoneErrorMessage(error: ValidationError?): String? = when (error) {
    ValidationError.EMPTY -> stringResource(Res.string.error_phone_empty)
    ValidationError.INVALID_FORMAT,
    ValidationError.TOO_LONG,
    ValidationError.TOO_SHORT -> stringResource(Res.string.error_phone_invalid_format)
    null -> null
}

@Composable
private fun hometownErrorMessage(error: ValidationError?): String? = when (error) {
    ValidationError.EMPTY,
    ValidationError.INVALID_FORMAT,
    ValidationError.TOO_LONG,
    ValidationError.TOO_SHORT -> stringResource(Res.string.error_generic_required)
    null -> null
}

private fun aadharErrorMessage(error: AuthValidationError?): String? = when (error) {
    AuthValidationError.EMPTY -> "Aadhar ID is required"
    AuthValidationError.INVALID_FORMAT,
    AuthValidationError.MISMATCH,
    AuthValidationError.TOO_SHORT -> "Enter a valid 12-digit Aadhar ID"
    null -> null
}

@Composable
private fun accountSaveErrorMessage(error: AuthResult.Failure): String = when (error.error) {
    com.harsh.rentalconnect.domain.model.AuthError.NETWORK -> stringResource(Res.string.error_network_message)
    com.harsh.rentalconnect.domain.model.AuthError.BACKEND_NOT_CONFIGURED -> stringResource(Res.string.error_backend_unavailable)
    else -> stringResource(Res.string.error_generic_message)
}
