package com.harsh.rentalconnect.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.harsh.rentalconnect.di.AppModule
import com.harsh.rentalconnect.domain.model.AuthSession
import com.harsh.rentalconnect.platform.openDialer
import com.harsh.rentalconnect.platform.openMapQuery
import com.harsh.rentalconnect.platform.openSms
import com.harsh.rentalconnect.platform.rememberPropertyPhotoPicker
import com.harsh.rentalconnect.ui.components.ErrorScreen
import com.harsh.rentalconnect.ui.models.Role
import com.harsh.rentalconnect.ui.screens.AccountScreen
import com.harsh.rentalconnect.ui.screens.AddPropertyScreen
import com.harsh.rentalconnect.ui.screens.AddTenantScreen
import com.harsh.rentalconnect.ui.screens.EmptyStateScreen
import com.harsh.rentalconnect.ui.screens.OnboardingScreen
import com.harsh.rentalconnect.ui.screens.OwnerHomeScreen
import com.harsh.rentalconnect.ui.screens.PropertyDetailOwnerScreen
import com.harsh.rentalconnect.ui.screens.PropertyDetailTenantScreen
import com.harsh.rentalconnect.ui.screens.SignInScreen
import com.harsh.rentalconnect.ui.screens.TenantHomeScreen
import com.harsh.rentalconnect.ui.screens.TenantProfileScreen
import com.harsh.rentalconnect.ui.viewmodel.AccountViewModel
import com.harsh.rentalconnect.ui.viewmodel.AddPropertyViewModel
import com.harsh.rentalconnect.ui.viewmodel.AddTenantViewModel
import com.harsh.rentalconnect.ui.viewmodel.OnboardingViewModel
import com.harsh.rentalconnect.ui.viewmodel.OwnerHomeUiState
import com.harsh.rentalconnect.ui.viewmodel.OwnerHomeViewModel
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailOwnerUiState
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailOwnerViewModel
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailTenantUiState
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailTenantViewModel
import com.harsh.rentalconnect.ui.viewmodel.SignInViewModel
import com.harsh.rentalconnect.ui.viewmodel.TenantHomeUiState
import com.harsh.rentalconnect.ui.viewmodel.TenantHomeViewModel
import com.harsh.rentalconnect.ui.viewmodel.TenantProfileUiState
import com.harsh.rentalconnect.ui.viewmodel.TenantProfileViewModel
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.empty_secondary_action
import rentalconnect.shared.generated.resources.empty_tenant_body
import rentalconnect.shared.generated.resources.empty_tenant_title

@Composable
fun AppNavGraph(
    session: AuthSession?,
) {
    when {
        session == null -> AuthNavGraph()
        session.user.role == Role.Owner -> OwnerNavGraph(session)
        else -> TenantNavGraph(session)
    }
}

@Composable
private fun AuthNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = AppRoute.Onboarding) {
        composable<AppRoute.Onboarding> {
            val viewModel = viewModel {
                OnboardingViewModel(
                    validateName = AppModule.validateNameUseCase,
                    validatePhone = AppModule.validatePhoneUseCase,
                    validateEmail = AppModule.validateEmailUseCase,
                    validatePassword = AppModule.validatePasswordUseCase,
                    validateAadhar = AppModule.validateAadharUseCase,
                    authRepository = AppModule.authRepository,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            OnboardingScreen(
                selectedRole = uiState.selectedRole,
                onRoleSelected = viewModel::onRoleSelected,
                phone = uiState.phone,
                onPhoneChange = viewModel::onPhoneChange,
                phoneError = uiState.phoneError,
                selectedCountryCode = uiState.selectedCountryCode,
                onCountryCodeChange = viewModel::onCountryCodeChange,
                name = uiState.name,
                onNameChange = viewModel::onNameChange,
                nameError = uiState.nameError,
                email = uiState.email,
                onEmailChange = viewModel::onEmailChange,
                emailError = uiState.emailError,
                password = uiState.password,
                onPasswordChange = viewModel::onPasswordChange,
                passwordError = uiState.passwordError,
                confirmPassword = uiState.confirmPassword,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                confirmPasswordError = uiState.confirmPasswordError,
                hometown = uiState.hometown,
                onHometownChange = viewModel::onHometownChange,
                hometownError = uiState.hometownError,
                aadharId = uiState.aadharId,
                onAadharChange = viewModel::onAadharChange,
                aadharError = uiState.aadharError,
                authError = uiState.authError,
                isSubmitting = uiState.isSubmitting,
                canContinue = uiState.canContinue,
                onContinue = viewModel::submit,
                onSignIn = { navController.navigate(AppRoute.SignIn) },
            )
        }

        composable<AppRoute.SignIn> {
            val viewModel = viewModel {
                SignInViewModel(
                    authRepository = AppModule.authRepository,
                    validateEmail = AppModule.validateEmailUseCase,
                    validatePassword = AppModule.validatePasswordUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SignInScreen(
                email = uiState.email,
                onEmailChange = viewModel::onEmailChange,
                emailError = uiState.emailError,
                password = uiState.password,
                onPasswordChange = viewModel::onPasswordChange,
                passwordError = uiState.passwordError,
                authError = uiState.authError,
                isSubmitting = uiState.isSubmitting,
                onSignIn = viewModel::submit,
                onCreateAccount = {
                    navController.navigate(AppRoute.Onboarding) {
                        popUpTo(AppRoute.SignIn) { inclusive = true }
                    }
                },
            )
        }
    }
}

@Composable
private fun OwnerNavGraph(
    session: AuthSession,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = AppRoute.OwnerHome) {
        composable<AppRoute.OwnerHome> {
            val viewModel = viewModel {
                OwnerHomeViewModel(
                    propertyRepository = AppModule.propertyRepository,
                    getProperties = AppModule.getPropertiesUseCase,
                    currentUser = session.user,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is OwnerHomeUiState.Loading -> LoadingScreen()
                is OwnerHomeUiState.Error -> ErrorScreen(message = state.message)
                is OwnerHomeUiState.Success -> OwnerHomeScreen(
                    userName = state.userName,
                    userInitials = state.userInitials,
                    stats = state.stats,
                    properties = state.properties,
                    selectedTab = 0,
                    onTabSelected = { tab ->
                        when (tab) {
                            1, 2 -> {
                                val firstProperty = state.properties.firstOrNull()
                                if (firstProperty == null) {
                                    navController.navigate(AppRoute.AddProperty)
                                } else {
                                    navController.navigate(AppRoute.PropertyDetailOwner(firstProperty.id))
                                }
                            }
                            3 -> navController.navigate(AppRoute.Account)
                        }
                    },
                    onSeeAllProperties = {
                        val firstProperty = state.properties.firstOrNull()
                        if (firstProperty == null) {
                            navController.navigate(AppRoute.AddProperty)
                        } else {
                            navController.navigate(AppRoute.PropertyDetailOwner(firstProperty.id))
                        }
                    },
                    onPropertyClick = { propertyId ->
                        navController.navigate(AppRoute.PropertyDetailOwner(propertyId))
                    },
                    onAddProperty = { navController.navigate(AppRoute.AddProperty) },
                    onAvatarClick = { navController.navigate(AppRoute.Account) },
                )
            }
        }

        composable<AppRoute.PropertyDetailOwner> { backStackEntry ->
            val route: AppRoute.PropertyDetailOwner = backStackEntry.toRoute()
            val viewModel = viewModel {
                PropertyDetailOwnerViewModel(
                    propertyId = route.propertyId,
                    propertyRepository = AppModule.propertyRepository,
                    getPropertyDetail = AppModule.getPropertyDetailUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is PropertyDetailOwnerUiState.Loading -> LoadingScreen()
                is PropertyDetailOwnerUiState.Error -> ErrorScreen(message = state.message)
                is PropertyDetailOwnerUiState.Success -> PropertyDetailOwnerScreen(
                    property = state.property,
                    selectedTab = 1,
                    onTabSelected = { tab ->
                        when (tab) {
                            0 -> navController.popBackStack(AppRoute.OwnerHome, false)
                            3 -> navController.navigate(AppRoute.Account)
                        }
                    },
                    onEditClick = { navController.navigate(AppRoute.EditProperty(route.propertyId)) },
                    onViewOnMap = { openMapQuery(state.property.address) },
                    onAddTenant = { navController.navigate(AppRoute.AddTenant(route.propertyId)) },
                    onTenantClick = { tenantId ->
                        navController.navigate(
                            AppRoute.TenantProfile(
                                propertyId = route.propertyId,
                                tenantId = tenantId,
                            )
                        )
                    },
                    onBackClick = { navController.popBackStack() },
                )
            }
        }

        composable<AppRoute.TenantProfile> { backStackEntry ->
            val route: AppRoute.TenantProfile = backStackEntry.toRoute()
            val viewModel = viewModel {
                TenantProfileViewModel(
                    tenantId = route.tenantId,
                    getTenantDetail = AppModule.getTenantDetailUseCase,
                    tenantRepository = AppModule.tenantRepository,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is TenantProfileUiState.Loading -> LoadingScreen()
                is TenantProfileUiState.Error -> ErrorScreen(message = state.message)
                is TenantProfileUiState.Success -> TenantProfileScreen(
                    tenant = state.tenant,
                    selectedTab = 2,
                    onTabSelected = { tab ->
                        when (tab) {
                            0 -> navController.popBackStack(AppRoute.OwnerHome, false)
                            1 -> navController.popBackStack(AppRoute.PropertyDetailOwner(route.propertyId), false)
                            3 -> navController.navigate(AppRoute.Account)
                        }
                    },
                    onRemove = { navController.popBackStack() },
                    onCallPhone = { openDialer(state.tenant.phone) },
                    onCallTenant = { openDialer(state.tenant.phone) },
                    onRemoveTenant = { viewModel.removeTenant { navController.popBackStack() } },
                    onBackClick = { navController.popBackStack() },
                )
            }
        }

        composable<AppRoute.AddProperty> {
            val viewModel = viewModel {
                AddPropertyViewModel(
                    propertyRepository = AppModule.propertyRepository,
                    addProperty = AppModule.addPropertyUseCase,
                    updateProperty = AppModule.updatePropertyUseCase,
                    currentUser = session.user,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val photoPicker = rememberPropertyPhotoPicker(
                onPhotosPicked = viewModel::uploadPickedPhotos,
                onError = viewModel::onPhotoUploadError,
            )

            AddPropertyScreen(
                isEditMode = false,
                propertyName = uiState.propertyName,
                onPropertyNameChange = viewModel::onPropertyNameChange,
                propertyNameError = uiState.propertyNameError,
                houseNumber = uiState.houseNumber,
                onHouseNumberChange = viewModel::onHouseNumberChange,
                houseNumberError = uiState.houseNumberError,
                fullAddress = uiState.fullAddress,
                onFullAddressChange = viewModel::onFullAddressChange,
                fullAddressError = uiState.fullAddressError,
                type = uiState.type,
                onTypeChange = viewModel::onTypeChange,
                typeError = uiState.typeError,
                photoUrlInput = uiState.photoUrlInput,
                onPhotoUrlInputChange = viewModel::onPhotoUrlInputChange,
                photoUrls = uiState.photoUrls,
                loadError = uiState.loadError,
                isUploadingPhotos = uiState.isUploadingPhotos,
                photoUploadError = uiState.photoUploadError,
                onDismissPhotoUploadError = viewModel::onPhotoUploadErrorDismissed,
                onAddPhotoUrl = viewModel::addPhotoUrl,
                onRemovePhotoUrl = viewModel::removePhotoUrl,
                isOccupied = uiState.isOccupied,
                onAvailabilityChange = viewModel::onAvailabilityChange,
                isSaving = uiState.isSaving,
                onAddPhotos = photoPicker,
                onTapToPin = {},
                onSave = { viewModel.save { navController.popBackStack() } },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<AppRoute.EditProperty> { backStackEntry ->
            val route: AppRoute.EditProperty = backStackEntry.toRoute()
            val viewModel = viewModel {
                AddPropertyViewModel(
                    propertyRepository = AppModule.propertyRepository,
                    addProperty = AppModule.addPropertyUseCase,
                    updateProperty = AppModule.updatePropertyUseCase,
                    currentUser = session.user,
                    propertyId = route.propertyId,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val photoPicker = rememberPropertyPhotoPicker(
                onPhotosPicked = viewModel::uploadPickedPhotos,
                onError = viewModel::onPhotoUploadError,
            )

            AddPropertyScreen(
                isEditMode = true,
                propertyName = uiState.propertyName,
                onPropertyNameChange = viewModel::onPropertyNameChange,
                propertyNameError = uiState.propertyNameError,
                houseNumber = uiState.houseNumber,
                onHouseNumberChange = viewModel::onHouseNumberChange,
                houseNumberError = uiState.houseNumberError,
                fullAddress = uiState.fullAddress,
                onFullAddressChange = viewModel::onFullAddressChange,
                fullAddressError = uiState.fullAddressError,
                type = uiState.type,
                onTypeChange = viewModel::onTypeChange,
                typeError = uiState.typeError,
                photoUrlInput = uiState.photoUrlInput,
                onPhotoUrlInputChange = viewModel::onPhotoUrlInputChange,
                photoUrls = uiState.photoUrls,
                loadError = uiState.loadError,
                isUploadingPhotos = uiState.isUploadingPhotos,
                photoUploadError = uiState.photoUploadError,
                onDismissPhotoUploadError = viewModel::onPhotoUploadErrorDismissed,
                onAddPhotoUrl = viewModel::addPhotoUrl,
                onRemovePhotoUrl = viewModel::removePhotoUrl,
                isOccupied = uiState.isOccupied,
                onAvailabilityChange = viewModel::onAvailabilityChange,
                isSaving = uiState.isSaving,
                onAddPhotos = photoPicker,
                onTapToPin = {},
                onSave = { viewModel.save { navController.popBackStack() } },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<AppRoute.AddTenant> { backStackEntry ->
            val route: AppRoute.AddTenant = backStackEntry.toRoute()
            val viewModel = viewModel {
                AddTenantViewModel(
                    propertyId = route.propertyId,
                    authRepository = AppModule.authRepository,
                    tenantRepository = AppModule.tenantRepository,
                    validatePhone = AppModule.validatePhoneUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddTenantScreen(
                phone = uiState.phone,
                phoneError = uiState.phoneError,
                selectedCountryCode = uiState.selectedCountryCode,
                onCountryCodeChange = viewModel::onCountryCodeChange,
                candidate = uiState.candidate,
                searchError = uiState.searchError,
                isSearching = uiState.isSearching,
                isConfirming = uiState.isConfirming,
                onPhoneChange = viewModel::onPhoneChange,
                onSearch = viewModel::search,
                onConfirm = { viewModel.confirm { navController.popBackStack() } },
                onBack = { navController.popBackStack() },
            )
        }

        composable<AppRoute.Account> {
            val viewModel = viewModel {
                AccountViewModel(
                    authRepository = AppModule.authRepository,
                    validateName = AppModule.validateNameUseCase,
                    validatePhone = AppModule.validatePhoneUseCase,
                    validateAadhar = AppModule.validateAadharUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            uiState.session?.let { sessionState ->
                AccountScreen(
                    userName = uiState.draftName,
                    userEmail = sessionState.user.email,
                    userPhone = uiState.draftPhone,
                    userHometown = uiState.draftHometown,
                    userAadharId = uiState.draftAadharId,
                    role = sessionState.user.role,
                    isEditing = uiState.isEditing,
                    isSaving = uiState.isSaving,
                    nameError = uiState.nameError,
                    phoneError = uiState.phoneError,
                    hometownError = uiState.hometownError,
                    aadharError = uiState.aadharError,
                    saveError = uiState.saveError,
                    onNameChange = viewModel::onNameChange,
                    onPhoneChange = viewModel::onPhoneChange,
                    onHometownChange = viewModel::onHometownChange,
                    onAadharChange = viewModel::onAadharChange,
                    onEdit = viewModel::startEditing,
                    onCancel = viewModel::cancelEditing,
                    onSave = viewModel::save,
                    onSignOut = viewModel::signOut,
                )
            }
        }
    }
}

@Composable
private fun TenantNavGraph(
    session: AuthSession,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = AppRoute.TenantHome) {
        composable<AppRoute.TenantHome> {
            val viewModel = viewModel {
                TenantHomeViewModel(
                    currentUser = session.user,
                    tenantRepository = AppModule.tenantRepository,
                    propertyRepository = AppModule.propertyRepository,
                    getTenantAssignments = AppModule.getTenantAssignmentsUseCase,
                    getProperties = AppModule.getPropertiesUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is TenantHomeUiState.Loading -> LoadingScreen()
                is TenantHomeUiState.Error -> ErrorScreen(message = state.message)
                is TenantHomeUiState.Empty -> EmptyStateScreen(
                    title = stringResource(Res.string.empty_tenant_title),
                    body = stringResource(Res.string.empty_tenant_body),
                    primaryActionLabel = stringResource(Res.string.empty_secondary_action),
                    onPrimaryAction = { navController.navigate(AppRoute.Account) },
                )
                is TenantHomeUiState.Success -> TenantHomeScreen(
                    userName = state.userName,
                    userInitials = state.userInitials,
                    rentals = state.rentals,
                    selectedTab = 0,
                    onTabSelected = { tab ->
                        when (tab) {
                            1 -> state.rentals.firstOrNull()?.let { rental ->
                                navController.navigate(AppRoute.PropertyDetailTenant(rental.propertyId))
                            }
                            2 -> navController.navigate(AppRoute.Account)
                        }
                    },
                    onViewPropertyDetails = { propertyId ->
                        navController.navigate(AppRoute.PropertyDetailTenant(propertyId))
                    },
                    onCallOwner = { phone -> openDialer(phone) },
                    onAvatarClick = { navController.navigate(AppRoute.Account) },
                )
            }
        }

        composable<AppRoute.PropertyDetailTenant> { backStackEntry ->
            val route: AppRoute.PropertyDetailTenant = backStackEntry.toRoute()
            val viewModel = viewModel {
                PropertyDetailTenantViewModel(
                    propertyId = route.propertyId,
                    currentUser = session.user,
                    tenantRepository = AppModule.tenantRepository,
                    propertyRepository = AppModule.propertyRepository,
                    getTenantAssignments = AppModule.getTenantAssignmentsUseCase,
                    getPropertyDetail = AppModule.getPropertyDetailUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is PropertyDetailTenantUiState.Loading -> LoadingScreen()
                is PropertyDetailTenantUiState.Error -> ErrorScreen(message = state.message)
                is PropertyDetailTenantUiState.Success -> PropertyDetailTenantScreen(
                    property = state.property,
                    owner = state.owner,
                    selectedTab = 1,
                    onTabSelected = { tab ->
                        when (tab) {
                            0 -> navController.popBackStack(AppRoute.TenantHome, false)
                            2 -> navController.navigate(AppRoute.Account)
                        }
                    },
                    onViewOnMap = { openMapQuery(state.property.address) },
                    onContactOwner = { openDialer(state.owner.phone) },
                    onBackClick = { navController.popBackStack() },
                )
            }
        }

        composable<AppRoute.Account> {
            val viewModel = viewModel {
                AccountViewModel(
                    authRepository = AppModule.authRepository,
                    validateName = AppModule.validateNameUseCase,
                    validatePhone = AppModule.validatePhoneUseCase,
                    validateAadhar = AppModule.validateAadharUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            uiState.session?.let { sessionState ->
                AccountScreen(
                    userName = uiState.draftName,
                    userEmail = sessionState.user.email,
                    userPhone = uiState.draftPhone,
                    userHometown = uiState.draftHometown,
                    userAadharId = uiState.draftAadharId,
                    role = sessionState.user.role,
                    isEditing = uiState.isEditing,
                    isSaving = uiState.isSaving,
                    nameError = uiState.nameError,
                    phoneError = uiState.phoneError,
                    hometownError = uiState.hometownError,
                    aadharError = uiState.aadharError,
                    saveError = uiState.saveError,
                    onNameChange = viewModel::onNameChange,
                    onPhoneChange = viewModel::onPhoneChange,
                    onHometownChange = viewModel::onHometownChange,
                    onAadharChange = viewModel::onAadharChange,
                    onEdit = viewModel::startEditing,
                    onCancel = viewModel::cancelEditing,
                    onSave = viewModel::save,
                    onSignOut = viewModel::signOut,
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
