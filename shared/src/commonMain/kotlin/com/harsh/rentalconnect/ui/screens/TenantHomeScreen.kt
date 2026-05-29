package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.rentalconnect.ui.models.OwnerInfo
import com.harsh.rentalconnect.ui.models.Role
import com.harsh.rentalconnect.ui.models.TenantPropertyInfo
import com.harsh.rentalconnect.ui.theme.AccentGreen
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.HomeBadgeBg
import com.harsh.rentalconnect.ui.theme.OnOccupied
import com.harsh.rentalconnect.ui.theme.OnPrimaryContainer
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OnVacant
import com.harsh.rentalconnect.ui.theme.OutlineSubtle
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.PrimaryMid
import com.harsh.rentalconnect.ui.theme.PrimarySubtle
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.SectionLabel
import com.harsh.rentalconnect.ui.theme.Surface
import com.harsh.rentalconnect.ui.theme.SurfaceMuted
import com.harsh.rentalconnect.ui.theme.VacantContainer
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.nav_home
import rentalconnect.shared.generated.resources.nav_profile
import rentalconnect.shared.generated.resources.nav_property
import rentalconnect.shared.generated.resources.onboarding_role_tenant
import rentalconnect.shared.generated.resources.tenant_home_call_button
import rentalconnect.shared.generated.resources.tenant_home_greeting
import rentalconnect.shared.generated.resources.tenant_home_message_button
import rentalconnect.shared.generated.resources.tenant_home_owner_role
import rentalconnect.shared.generated.resources.tenant_home_owner_section
import rentalconnect.shared.generated.resources.tenant_home_property_section
import rentalconnect.shared.generated.resources.tenant_home_view_details
import rentalconnect.shared.generated.resources.tenant_home_your_home_badge

// ---------------------------------------------------------------------------
// TenantHomeScreen
// ---------------------------------------------------------------------------

@Composable
fun TenantHomeScreen(
    userName: String,
    userInitials: String,
    property: TenantPropertyInfo,
    owner: OwnerInfo,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onViewPropertyDetails: () -> Unit,
    onCallOwner: () -> Unit,
    onMessageOwner: () -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            // Dark green status accent bar at the very top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppSpacing.xs)
                    .background(AccentGreen),
            )

            // Main content with horizontal padding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.xl),
            ) {
                Spacer(modifier = Modifier.height(AppSpacing.xl))

                // ---- Header row: greeting + avatar ----
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

                    // Circular avatar
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

                Spacer(modifier = Modifier.height(28.dp))

                // ---- MY PROPERTY section ----
                SectionLabelText(text = stringResource(Res.string.tenant_home_property_section))

                Spacer(modifier = Modifier.height(10.dp))

                PropertyCard(
                    property = property,
                    onViewDetails = onViewPropertyDetails,
                )

                Spacer(modifier = Modifier.height(AppSpacing.xxl))

                // ---- MY OWNER section ----
                SectionLabelText(text = stringResource(Res.string.tenant_home_owner_section))

                Spacer(modifier = Modifier.height(10.dp))

                OwnerCard(
                    owner = owner,
                    onCall = onCallOwner,
                    onMessage = onMessageOwner,
                )

                Spacer(modifier = Modifier.height(AppSpacing.xxl))
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Section label
// ---------------------------------------------------------------------------

@Composable
private fun SectionLabelText(text: String) {
    Text(
        text = text,
        style = RentalConnectTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
        ),
        color = SectionLabel,
    )
}

// ---------------------------------------------------------------------------
// Property card
// ---------------------------------------------------------------------------

@Composable
private fun PropertyCard(
    property: TenantPropertyInfo,
    onViewDetails: () -> Unit,
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
        // Image placeholder with "Your home" badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSize.propertyImageMd)
                .background(PrimarySubtle),
            contentAlignment = Alignment.TopEnd,
        ) {
            // "Your home" badge — top-right
            Box(
                modifier = Modifier
                    .padding(top = AppSpacing.md, end = AppSpacing.md)
                    .clip(RoundedCornerShape(20.dp))
                    .background(HomeBadgeBg)
                    .padding(horizontal = AppSpacing.md, vertical = 5.dp),
            ) {
                Text(
                    text = stringResource(Res.string.tenant_home_your_home_badge),
                    style = RentalConnectTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                    color = OnOccupied,
                )
            }
        }

        // Card body
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.lg, vertical = 14.dp),
        ) {
            Text(
                text = property.propertyName,
                style = RentalConnectTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = OnSurface,
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            Text(
                text = property.address,
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(AppSpacing.md))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${property.flatNumber} · ${property.type}",
                    style = RentalConnectTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                )

                Text(
                    text = stringResource(Res.string.tenant_home_view_details),
                    style = RentalConnectTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = Primary,
                    modifier = Modifier.clickable(onClick = onViewDetails),
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Owner card
// ---------------------------------------------------------------------------

@Composable
private fun OwnerCard(
    owner: OwnerInfo,
    onCall: () -> Unit,
    onMessage: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.lg))
            .background(Surface)
            .border(
                width = 1.dp,
                color = SurfaceMuted,
                shape = RoundedCornerShape(AppRadius.lg),
            )
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Circular avatar
        Box(
            modifier = Modifier
                .size(AppSize.avatarMd)
                .clip(CircleShape)
                .background(PrimaryMid),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = owner.initials,
                style = RentalConnectTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                ),
                color = OnPrimaryContainer,
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Name + role
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = owner.name,
                style = RentalConnectTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = OnSurface,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = when (owner.role) {
                    Role.Owner -> stringResource(Res.string.tenant_home_owner_role)
                    Role.Tenant -> stringResource(Res.string.onboarding_role_tenant)
                },
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
        }

        // Action pill buttons
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            OwnerPillButton(
                label = stringResource(Res.string.tenant_home_call_button),
                onClick = onCall,
            )
            OwnerPillButton(
                label = stringResource(Res.string.tenant_home_message_button),
                onClick = onMessage,
            )
        }
    }
}

@Composable
private fun OwnerPillButton(
    label: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(AppRadius.pill))
            .border(
                width = 1.dp,
                color = OutlineSubtle,
                shape = RoundedCornerShape(AppRadius.pill),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = RentalConnectTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
            ),
            color = OnSurface,
        )
    }
}

// ---------------------------------------------------------------------------
// Bottom navigation
// ---------------------------------------------------------------------------

@Composable
private fun TenantBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabLabels = listOf(
        stringResource(Res.string.nav_home),
        stringResource(Res.string.nav_property),
        stringResource(Res.string.nav_profile),
    )

    NavigationBar(
        containerColor = Surface,
        tonalElevation = 0.dp,
    ) {
        tabLabels.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = {
                    // Icon placeholder — colored dot or letter badge
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(
                                if (selectedTab == index) Primary.copy(alpha = 0.15f)
                                else Color.Transparent,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = label.first().toString(),
                            style = RentalConnectTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                            ),
                            color = if (selectedTab == index) Primary else OnSurfaceVariant,
                        )
                    }
                },
                label = {
                    Text(
                        text = label,
                        style = RentalConnectTheme.typography.labelMedium.copy(
                            fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                        ),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview
@Composable
fun TenantHomeScreenPreview() {
    RentalConnectTheme {
        TenantHomeScreen(
            userName = "Arjun Khanna",
            userInitials = "AK",
            property = TenantPropertyInfo(
                propertyName = "Sunrise Apartments",
                address = "14B, MG Road, Bengaluru",
                flatNumber = "Flat 2A",
                type = "2BHK",
            ),
            owner = OwnerInfo(
                name = "Rajesh Kumar",
                initials = "RK",
                role = Role.Owner,
            ),
            selectedTab = 0,
            onTabSelected = {},
            onViewPropertyDetails = {},
            onCallOwner = {},
            onMessageOwner = {},
            onAvatarClick = {},
        )
    }
}
