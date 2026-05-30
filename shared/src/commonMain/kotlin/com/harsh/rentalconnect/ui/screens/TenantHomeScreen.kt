package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.harsh.rentalconnect.ui.components.NavItem
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.rentalconnect.ui.models.TenantPropertyInfo
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.OnOccupied
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OnVacant
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.PrimarySubtle
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import com.harsh.rentalconnect.ui.theme.SurfaceMuted
import com.harsh.rentalconnect.ui.theme.VacantContainer
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.nav_home
import rentalconnect.shared.generated.resources.nav_profile
import rentalconnect.shared.generated.resources.nav_property
import rentalconnect.shared.generated.resources.tenant_home_call_button
import rentalconnect.shared.generated.resources.tenant_home_greeting
import rentalconnect.shared.generated.resources.tenant_home_owner_prefix
import rentalconnect.shared.generated.resources.tenant_home_rentals_section
import rentalconnect.shared.generated.resources.tenant_home_view_details
import rentalconnect.shared.generated.resources.tenant_home_your_home_badge

@Composable
fun TenantHomeScreen(
    userName: String,
    userInitials: String,
    rentals: List<TenantPropertyInfo>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onViewPropertyDetails: (String) -> Unit,
    onCallOwner: (String) -> Unit,
    onAvatarClick: () -> Unit,
) {
    Scaffold(
        containerColor = Background,
        bottomBar = {
            TenantBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = AppSpacing.xl, vertical = AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = stringResource(Res.string.tenant_home_greeting),
                            style = RentalConnectTheme.typography.bodyMedium,
                            color = OnSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = userName,
                            style = RentalConnectTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                            ),
                            color = OnSurface,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(VacantContainer)
                            .clickable(onClick = onAvatarClick),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = userInitials,
                            style = RentalConnectTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                            ),
                            color = OnVacant,
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(Res.string.tenant_home_rentals_section),
                    style = RentalConnectTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                    ),
                    color = OnSurfaceVariant,
                )
            }

            items(rentals, key = { it.propertyId }) { rental ->
                RentalCard(
                    rental = rental,
                    highlightAsHome = rentals.size == 1,
                    onViewDetails = { onViewPropertyDetails(rental.propertyId) },
                    onCallOwner = { onCallOwner(rental.ownerPhone) },
                )
            }
        }
    }
}

@Composable
private fun RentalCard(
    rental: TenantPropertyInfo,
    highlightAsHome: Boolean,
    onViewDetails: () -> Unit,
    onCallOwner: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.lg))
            .background(Surface)
            .border(
                width = 1.dp,
                color = SurfaceMuted,
                shape = RoundedCornerShape(AppRadius.lg),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSize.propertyImageMd)
                .background(PrimarySubtle),
            contentAlignment = Alignment.TopEnd,
        ) {
            if (highlightAsHome) {
                Box(
                    modifier = Modifier
                        .padding(top = AppSpacing.md, end = AppSpacing.md)
                        .clip(RoundedCornerShape(20.dp))
                        .background(VacantContainer)
                        .padding(horizontal = AppSpacing.md, vertical = 5.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.tenant_home_your_home_badge),
                        style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = OnOccupied,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
        ) {
            Text(
                text = rental.propertyName,
                style = RentalConnectTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )
            Text(
                text = rental.address,
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                Chip(if (rental.isOccupied) "Occupied" else "Vacant")
                Chip(rental.houseNumber)
                Chip(rental.flatNumber)
            }
            Text(
                text = "${stringResource(Res.string.tenant_home_owner_prefix)}: ${rental.ownerName}",
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                Text(
                    text = stringResource(Res.string.tenant_home_view_details),
                    style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Primary,
                    modifier = Modifier.clickable(onClick = onViewDetails),
                )
                Text(
                    text = stringResource(Res.string.tenant_home_call_button),
                    style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Primary,
                    modifier = Modifier.clickable(onClick = onCallOwner),
                )
            }
        }
    }
}

@Composable
private fun Chip(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(AppRadius.pill))
            .background(VacantContainer)
            .padding(horizontal = AppSpacing.md, vertical = 6.dp),
    ) {
        Text(
            text = label,
            style = RentalConnectTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = OnVacant,
        )
    }
}

@Composable
private fun TenantBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    val navItems = listOf(
        NavItem(stringResource(Res.string.nav_home), Icons.Filled.Home, Icons.Outlined.Home),
        NavItem(stringResource(Res.string.nav_property), Icons.Filled.LocationOn, Icons.Outlined.LocationOn),
        NavItem(stringResource(Res.string.nav_profile), Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle),
    )

    NavigationBar(
        containerColor = Surface,
        tonalElevation = 0.dp,
    ) {
        navItems.forEachIndexed { index, item ->
            val selected = index == selectedTab
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp),
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = RentalConnectTheme.typography.labelMedium.copy(
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        ),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Primary,
                    indicatorColor = Primary.copy(alpha = 0.12f),
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                ),
            )
        }
    }
}
