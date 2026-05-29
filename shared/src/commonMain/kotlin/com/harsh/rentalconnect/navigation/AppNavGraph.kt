package com.harsh.rentalconnect.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.harsh.rentalconnect.ui.components.ErrorScreen
import com.harsh.rentalconnect.ui.screens.AddPropertyScreen
import com.harsh.rentalconnect.ui.screens.OnboardingScreen
import com.harsh.rentalconnect.ui.screens.OwnerHomeScreen
import com.harsh.rentalconnect.ui.screens.SignInScreen
import com.harsh.rentalconnect.ui.screens.PropertyDetailOwnerScreen
import com.harsh.rentalconnect.ui.screens.PropertyDetailTenantScreen
import com.harsh.rentalconnect.ui.screens.TenantHomeScreen
import com.harsh.rentalconnect.ui.screens.TenantProfileScreen
import com.harsh.rentalconnect.ui.models.Role
import com.harsh.rentalconnect.ui.viewmodel.AddPropertyViewModel
import com.harsh.rentalconnect.ui.viewmodel.OnboardingViewModel
import com.harsh.rentalconnect.ui.viewmodel.OwnerHomeUiState
import com.harsh.rentalconnect.ui.viewmodel.OwnerHomeViewModel
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailOwnerUiState
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailOwnerViewModel
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailTenantUiState
import com.harsh.rentalconnect.ui.viewmodel.PropertyDetailTenantViewModel
import com.harsh.rentalconnect.ui.viewmodel.TenantHomeUiState
import com.harsh.rentalconnect.ui.viewmodel.TenantHomeViewModel
import com.harsh.rentalconnect.ui.viewmodel.TenantProfileUiState
import com.harsh.rentalconnect.ui.viewmodel.TenantProfileViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Onboarding,
    ) {

        // ── Onboarding ────────────────────────────────────────────────────────
        composable<AppRoute.Onboarding> {
            val viewModel = viewModel {
                OnboardingViewModel(AppModule.validateNameUseCase, AppModule.validatePhoneUseCase)
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            OnboardingScreen(
                selectedRole = uiState.selectedRole,
                onRoleSelected = viewModel::onRoleSelected,
                phone = uiState.phone,
                onPhoneChange = viewModel::onPhoneChange,
                phoneError = uiState.phoneError,
                name = uiState.name,
                onNameChange = viewModel::onNameChange,
                nameError = uiState.nameError,
                canContinue = uiState.canContinue,
                onContinue = {
                    if (viewModel.onContinueAttempted()) {
                        if (uiState.selectedRole == Role.Owner) {
                            navController.navigate(AppRoute.OwnerHome) {
                                popUpTo(AppRoute.Onboarding) { inclusive = true }
                            }
                        } else {
                            navController.navigate(AppRoute.TenantHome) {
                                popUpTo(AppRoute.Onboarding) { inclusive = true }
                            }
                        }
                    }
                },
                onSignIn = { navController.navigate(AppRoute.SignIn) },
            )
        }

        // ── Sign In ───────────────────────────────────────────────────────────
        composable<AppRoute.SignIn> {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            SignInScreen(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                onSignIn = { /* TODO: call auth */ },
                onCreateAccount = {
                    navController.navigate(AppRoute.Onboarding) {
                        popUpTo(AppRoute.SignIn) { inclusive = true }
                    }
                },
            )
        }

        // ── Owner: Home ───────────────────────────────────────────────────────
        composable<AppRoute.OwnerHome> {
            val viewModel = viewModel { OwnerHomeViewModel(AppModule.getPropertiesUseCase) }
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
                            1 -> state.properties.firstOrNull()?.let { first ->
                                navController.navigate(AppRoute.PropertyDetailOwner(first.id)) {
                                    launchSingleTop = true
                                }
                            }
                            else -> Unit
                        }
                    },
                    onSeeAllProperties = {
                        state.properties.firstOrNull()?.let { first ->
                            navController.navigate(AppRoute.PropertyDetailOwner(first.id))
                        }
                    },
                    onPropertyClick = { propertyId ->
                        navController.navigate(AppRoute.PropertyDetailOwner(propertyId))
                    },
                    onAvatarClick = { /* TODO: owner profile */ },
                )
            }
        }

        // ── Owner: Property Detail ────────────────────────────────────────────
        composable<AppRoute.PropertyDetailOwner> { backStackEntry ->
            val route: AppRoute.PropertyDetailOwner = backStackEntry.toRoute()
            val viewModel = viewModel {
                PropertyDetailOwnerViewModel(route.propertyId, AppModule.getPropertyDetailUseCase)
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
                            0 -> navController.navigate(AppRoute.OwnerHome) {
                                popUpTo(AppRoute.OwnerHome) { inclusive = false }
                                launchSingleTop = true
                            }
                            else -> Unit
                        }
                    },
                    onEditClick = { /* TODO: edit property */ },
                    onViewOnMap = { /* TODO: open map */ },
                    onAddTenant = { /* TODO: add tenant flow */ },
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

        // ── Owner: Tenant Profile ─────────────────────────────────────────────
        composable<AppRoute.TenantProfile> { backStackEntry ->
            val route: AppRoute.TenantProfile = backStackEntry.toRoute()
            val viewModel = viewModel {
                TenantProfileViewModel(route.tenantId, AppModule.getTenantDetailUseCase)
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
                            0 -> navController.navigate(AppRoute.OwnerHome) {
                                popUpTo(AppRoute.OwnerHome) { inclusive = false }
                                launchSingleTop = true
                            }
                            1 -> navController.navigate(AppRoute.PropertyDetailOwner(route.propertyId)) {
                                popUpTo(AppRoute.PropertyDetailOwner(route.propertyId)) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                            else -> Unit
                        }
                    },
                    onRemove = { navController.popBackStack() },
                    onCallPhone = { /* TODO: dial intent */ },
                    onCallTenant = { /* TODO: dial intent */ },
                    onRemoveTenant = { navController.popBackStack() },
                    onBackClick = { navController.popBackStack() },
                )
            }
        }

        // ── Owner: Add Property ───────────────────────────────────────────────
        composable<AppRoute.AddProperty> {
            val viewModel = viewModel { AddPropertyViewModel() }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AddPropertyScreen(
                propertyName = uiState.propertyName,
                onPropertyNameChange = viewModel::onPropertyNameChange,
                houseNumber = uiState.houseNumber,
                onHouseNumberChange = viewModel::onHouseNumberChange,
                fullAddress = uiState.fullAddress,
                onFullAddressChange = viewModel::onFullAddressChange,
                type = uiState.type,
                onTypeChange = viewModel::onTypeChange,
                isOccupied = uiState.isOccupied,
                onAvailabilityChange = viewModel::onAvailabilityChange,
                onAddPhotos = { /* TODO: media picker */ },
                onTapToPin = { /* TODO: map picker */ },
                onSave = { viewModel.save { navController.popBackStack() } },
                onBackClick = { navController.popBackStack() },
            )
        }

        // ── Tenant: Home ──────────────────────────────────────────────────────
        composable<AppRoute.TenantHome> {
            val viewModel = viewModel {
                TenantHomeViewModel(
                    getTenantDetail = AppModule.getTenantDetailUseCase,
                    getPropertyDetail = AppModule.getPropertyDetailUseCase,
                )
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is TenantHomeUiState.Loading -> LoadingScreen()
                is TenantHomeUiState.Error -> ErrorScreen(message = state.message)
                is TenantHomeUiState.Success -> TenantHomeScreen(
                    userName = state.userName,
                    userInitials = state.userInitials,
                    property = state.property,
                    owner = state.owner,
                    selectedTab = 0,
                    onTabSelected = { tab ->
                        when (tab) {
                            1 -> navController.navigate(AppRoute.PropertyDetailTenant) {
                                launchSingleTop = true
                            }
                            else -> Unit
                        }
                    },
                    onViewPropertyDetails = { navController.navigate(AppRoute.PropertyDetailTenant) },
                    onCallOwner = { /* TODO: dial intent */ },
                    onMessageOwner = { /* TODO: messaging */ },
                    onAvatarClick = { /* TODO: tenant profile */ },
                )
            }
        }

        // ── Tenant: Property Detail ───────────────────────────────────────────
        composable<AppRoute.PropertyDetailTenant> {
            val viewModel = viewModel {
                PropertyDetailTenantViewModel(
                    getTenantDetail = AppModule.getTenantDetailUseCase,
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
                            0 -> navController.navigate(AppRoute.TenantHome) {
                                popUpTo(AppRoute.TenantHome) { inclusive = false }
                                launchSingleTop = true
                            }
                            else -> Unit
                        }
                    },
                    onViewOnMap = { /* TODO: open map */ },
                    onContactOwner = { /* TODO: dial intent */ },
                    onBackClick = { navController.popBackStack() },
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
