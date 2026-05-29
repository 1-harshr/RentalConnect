package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.rentalconnect.ui.models.PropertyDetail
import com.harsh.rentalconnect.ui.models.TenantSummary
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.AvatarAmber
import com.harsh.rentalconnect.ui.theme.AvatarCyan
import com.harsh.rentalconnect.ui.theme.AvatarIndigo
import com.harsh.rentalconnect.ui.theme.AvatarPink
import com.harsh.rentalconnect.ui.theme.AvatarTeal
import com.harsh.rentalconnect.ui.theme.AvatarViolet
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.Divider
import com.harsh.rentalconnect.ui.theme.IconTintIndigo
import com.harsh.rentalconnect.ui.theme.IconTintOrange
import com.harsh.rentalconnect.ui.theme.OnPrimaryContainer
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OnVacant
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.PrimarySubtle
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import com.harsh.rentalconnect.ui.theme.VacantContainer
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.cd_back
import rentalconnect.shared.generated.resources.nav_home
import rentalconnect.shared.generated.resources.nav_profile
import rentalconnect.shared.generated.resources.nav_properties
import rentalconnect.shared.generated.resources.nav_tenants
import rentalconnect.shared.generated.resources.property_detail_add_tenant
import rentalconnect.shared.generated.resources.property_detail_edit
import rentalconnect.shared.generated.resources.property_detail_house_number_label
import rentalconnect.shared.generated.resources.property_detail_map_label
import rentalconnect.shared.generated.resources.property_detail_type_label
import rentalconnect.shared.generated.resources.property_detail_view_on_map
import rentalconnect.shared.generated.resources.status_occupied

// ---------------------------------------------------------------------------
// Avatar color palette for tenants
// ---------------------------------------------------------------------------

val avatarColors = listOf(
    AvatarIndigo,
    AvatarTeal,
    AvatarAmber,
    AvatarPink,
    AvatarViolet,
    AvatarCyan,
)

private fun avatarColorFor(index: Int): Color = avatarColors[index % avatarColors.size]

// ---------------------------------------------------------------------------
// Bottom-nav tab descriptor
// ---------------------------------------------------------------------------

private data class NavTab(val label: String, val icon: ImageVector)

// ---------------------------------------------------------------------------
// Main screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailOwnerScreen(
    property: PropertyDetail,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onEditClick: () -> Unit,
    onViewOnMap: () -> Unit,
    onAddTenant: () -> Unit,
    onTenantClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val navTabs = listOf(
        NavTab(stringResource(Res.string.nav_home), Icons.Filled.Home),
        NavTab(stringResource(Res.string.nav_properties), Icons.Filled.LocationOn),
        NavTab(stringResource(Res.string.nav_tenants), Icons.Filled.AccountCircle),
        NavTab(stringResource(Res.string.nav_profile), Icons.Filled.Person),
    )

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_back),
                            tint = OnSurface,
                        )
                    }
                },
                title = {
                    Text(
                        text = property.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = OnSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                actions = {
                    TextButton(onClick = onEditClick) {
                        Text(
                            text = stringResource(Res.string.property_detail_edit),
                            color = Primary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                ),
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Surface) {
                navTabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                            )
                        },
                        label = { Text(tab.label, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Primary,
                            selectedTextColor = Primary,
                            indicatorColor = Primary.copy(alpha = 0.12f),
                            unselectedIconColor = OnSurfaceVariant,
                            unselectedTextColor = OnSurfaceVariant,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            // ----------------------------------------------------------------
            // Image carousel placeholder
            // ----------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(PrimarySubtle), // light blue
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Photo",
                    color = OnPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                )
            }

            // Pagination dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (i == 0) AppSize.dotActive else AppSize.dotInactive)
                            .clip(CircleShape)
                            .background(if (i == 0) Primary else OnSurfaceVariant.copy(alpha = 0.35f)),
                    )
                }
            }

            // ----------------------------------------------------------------
            // Property name + status badge
            // ----------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
            ) {
                Text(
                    text = property.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = OnSurface,
                )
                if (property.isOccupied) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(AppRadius.pill))
                            .background(VacantContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.status_occupied),
                            color = OnVacant,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = AppSpacing.xs),
                        )
                    }
                }
            }

            Spacer(Modifier.height(AppSpacing.xs))

            // Address
            Text(
                text = property.address,
                color = OnSurfaceVariant,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = AppSpacing.lg),
            )

            Spacer(Modifier.height(AppSpacing.xl))

            // ----------------------------------------------------------------
            // Info card
            // ----------------------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg)
                    .clip(RoundedCornerShape(AppRadius.md))
                    .background(Surface)
                    .border(1.dp, Divider, RoundedCornerShape(AppRadius.md)),
            ) {
                InfoRow(
                    iconColor = IconTintIndigo,
                    iconContent = {
                        Text("T", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    },
                    label = stringResource(Res.string.property_detail_type_label),
                    value = property.type,
                )
                androidx.compose.material3.Divider(color = Divider, thickness = 0.5.dp)
                InfoRow(
                    iconColor = IconTintOrange,
                    iconContent = {
                        Text("#", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    },
                    label = stringResource(Res.string.property_detail_house_number_label),
                    value = property.houseNumber,
                )
                androidx.compose.material3.Divider(color = Divider, thickness = 0.5.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(AppSize.avatarSm)
                            .clip(RoundedCornerShape(AppRadius.sm))
                            .background(AvatarTeal),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("M", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(AppSpacing.md))
                    Text(
                        text = stringResource(Res.string.property_detail_map_label),
                        color = OnSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = stringResource(Res.string.property_detail_view_on_map),
                        color = Primary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable(onClick = onViewOnMap),
                    )
                }
            }

            Spacer(Modifier.height(AppSpacing.xxl))

            // ----------------------------------------------------------------
            // Tenants section header
            // ----------------------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "TENANTS (${property.tenants.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = OnSurfaceVariant,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onAddTenant) {
                    Text(
                        text = stringResource(Res.string.property_detail_add_tenant),
                        color = Primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                }
            }

            Spacer(Modifier.height(AppSpacing.xs))

            // ----------------------------------------------------------------
            // Tenant rows
            // ----------------------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg)
                    .clip(RoundedCornerShape(AppRadius.md))
                    .background(Surface)
                    .border(1.dp, Divider, RoundedCornerShape(AppRadius.md)),
            ) {
                property.tenants.forEachIndexed { index, tenant ->
                    TenantRow(
                        tenant = tenant,
                        avatarColor = avatarColorFor(index),
                        onClick = { onTenantClick(tenant.id) },
                    )
                    if (index < property.tenants.lastIndex) {
                        androidx.compose.material3.Divider(color = Divider, thickness = 0.5.dp)
                    }
                }
            }

            Spacer(Modifier.height(AppSpacing.xxl))
        }
    }
}

// ---------------------------------------------------------------------------
// Private sub-composables
// ---------------------------------------------------------------------------

@Composable
private fun InfoRow(
    iconColor: Color,
    iconContent: @Composable () -> Unit,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppSize.avatarSm)
                .clip(RoundedCornerShape(AppRadius.sm))
                .background(iconColor),
            contentAlignment = Alignment.Center,
        ) {
            iconContent()
        }
        Spacer(Modifier.width(AppSpacing.md))
        Text(
            text = label,
            color = OnSurfaceVariant,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            color = OnSurface,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun TenantRow(
    tenant: TenantSummary,
    avatarColor: Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Circular avatar with initials
        Box(
            modifier = Modifier
                .size(AppSize.iconBox)
                .clip(CircleShape)
                .background(avatarColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = tenant.initials,
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
        }
        Spacer(Modifier.width(AppSpacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tenant.name,
                color = OnSurface,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Flat ${tenant.flatNumber} · Since ${tenant.since}",
                color = OnSurfaceVariant,
                fontSize = 13.sp,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview
@Composable
fun PropertyDetailOwnerScreenPreview() {
    RentalConnectTheme {
        PropertyDetailOwnerScreen(
            property = PropertyDetail(
                id = "prop-001",
                name = "Sunrise Apartments",
                address = "14B, MG Road, Bengaluru 560001",
                type = "2BHK Apartment",
                houseNumber = "HNO-14B",
                isOccupied = true,
                tenants = listOf(
                    TenantSummary(
                        id = "t1",
                        name = "Ananya Sharma",
                        initials = "AS",
                        flatNumber = "2A",
                        since = "Jan 2023",
                    ),
                    TenantSummary(
                        id = "t2",
                        name = "Rohan Mehta",
                        initials = "RM",
                        flatNumber = "2B",
                        since = "Mar 2023",
                    ),
                    TenantSummary(
                        id = "t3",
                        name = "Priya Nair",
                        initials = "PN",
                        flatNumber = "3A",
                        since = "Jun 2024",
                    ),
                ),
            ),
            selectedTab = 1,
            onTabSelected = {},
            onEditClick = {},
            onViewOnMap = {},
            onAddTenant = {},
            onTenantClick = {},
            onBackClick = {},
        )
    }
}
