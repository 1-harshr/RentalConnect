package com.harsh.rentalconnect.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.harsh.rentalconnect.ui.screens.AddPropertyScreen
import com.harsh.rentalconnect.ui.screens.OnboardingScreen
import com.harsh.rentalconnect.ui.screens.OwnerHomeScreen
import com.harsh.rentalconnect.ui.screens.PropertyDetailOwnerScreen
import com.harsh.rentalconnect.ui.screens.PropertyDetailTenantScreen
import com.harsh.rentalconnect.ui.screens.TenantHomeScreen
import com.harsh.rentalconnect.ui.screens.TenantProfileScreen

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
            var selectedRole by remember { mutableStateOf("owner") }
            var phone by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }

            OnboardingScreen(
                selectedRole = selectedRole,
                onRoleSelected = { selectedRole = it },
                phone = phone,
                onPhoneChange = { phone = it },
                name = name,
                onNameChange = { name = it },
                onContinue = {
                    if (selectedRole == "owner") {
                        navController.navigate(AppRoute.OwnerHome) {
                            popUpTo(AppRoute.Onboarding) { inclusive = true }
                        }
                    } else {
                        navController.navigate(AppRoute.TenantHome) {
                            popUpTo(AppRoute.Onboarding) { inclusive = true }
                        }
                    }
                },
                onSignIn = {
                    // TODO: navigate to sign-in screen
                },
            )
        }

        // ── Owner: Home ───────────────────────────────────────────────────────
        composable<AppRoute.OwnerHome> {
            OwnerHomeScreen(
                userName = SampleData.ownerName,
                userInitials = SampleData.ownerInitials,
                stats = SampleData.ownerStats,
                properties = SampleData.properties,
                selectedTab = 0,
                onTabSelected = { tab ->
                    when (tab) {
                        1 -> navController.navigate(
                            AppRoute.PropertyDetailOwner(SampleData.properties.first().id)
                        ) { launchSingleTop = true }
                        // Tabs 2 & 3 (Tenants, Profile) — screens not yet designed
                        else -> Unit
                    }
                },
                onSeeAllProperties = {
                    navController.navigate(
                        AppRoute.PropertyDetailOwner(SampleData.properties.first().id)
                    )
                },
                onPropertyClick = { propertyId ->
                    navController.navigate(AppRoute.PropertyDetailOwner(propertyId))
                },
                onAvatarClick = { /* TODO: owner profile */ },
            )
        }

        // ── Owner: Property Detail ────────────────────────────────────────────
        composable<AppRoute.PropertyDetailOwner> { backStackEntry ->
            val route: AppRoute.PropertyDetailOwner = backStackEntry.toRoute()
            val property = SampleData.propertyDetailFor(route.propertyId)

            PropertyDetailOwnerScreen(
                property = property,
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

        // ── Owner: Tenant Profile ─────────────────────────────────────────────
        composable<AppRoute.TenantProfile> { backStackEntry ->
            val route: AppRoute.TenantProfile = backStackEntry.toRoute()
            val tenant = SampleData.tenantDetailFor(route.tenantId)

            TenantProfileScreen(
                tenant = tenant,
                selectedTab = 2,
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> navController.navigate(AppRoute.OwnerHome) {
                            popUpTo(AppRoute.OwnerHome) { inclusive = false }
                            launchSingleTop = true
                        }
                        1 -> navController.navigate(
                            AppRoute.PropertyDetailOwner(route.propertyId)
                        ) {
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

        // ── Owner: Add Property ───────────────────────────────────────────────
        composable<AppRoute.AddProperty> {
            var propertyName by remember { mutableStateOf("") }
            var houseNumber by remember { mutableStateOf("") }
            var fullAddress by remember { mutableStateOf("") }
            var type by remember { mutableStateOf("") }
            var isOccupied by remember { mutableStateOf(true) }

            AddPropertyScreen(
                propertyName = propertyName,
                onPropertyNameChange = { propertyName = it },
                houseNumber = houseNumber,
                onHouseNumberChange = { houseNumber = it },
                fullAddress = fullAddress,
                onFullAddressChange = { fullAddress = it },
                type = type,
                onTypeChange = { type = it },
                isOccupied = isOccupied,
                onAvailabilityChange = { isOccupied = it },
                onAddPhotos = { /* TODO: media picker */ },
                onTapToPin = { /* TODO: map picker */ },
                onSave = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
            )
        }

        // ── Tenant: Home ──────────────────────────────────────────────────────
        composable<AppRoute.TenantHome> {
            TenantHomeScreen(
                userName = SampleData.tenantName,
                userInitials = SampleData.tenantInitials,
                property = SampleData.tenantPropertyInfo,
                owner = SampleData.tenantOwnerInfo,
                selectedTab = 0,
                onTabSelected = { tab ->
                    when (tab) {
                        1 -> navController.navigate(AppRoute.PropertyDetailTenant) {
                            launchSingleTop = true
                        }
                        // Tab 2 (Profile) — screen not yet designed
                        else -> Unit
                    }
                },
                onViewPropertyDetails = {
                    navController.navigate(AppRoute.PropertyDetailTenant)
                },
                onCallOwner = { /* TODO: dial intent */ },
                onMessageOwner = { /* TODO: messaging */ },
                onAvatarClick = { /* TODO: tenant profile */ },
            )
        }

        // ── Tenant: Property Detail ───────────────────────────────────────────
        composable<AppRoute.PropertyDetailTenant> {
            PropertyDetailTenantScreen(
                property = SampleData.tenantPropertyDetail,
                owner = SampleData.tenantOwnerContact,
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
