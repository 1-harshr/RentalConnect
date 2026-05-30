package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.harsh.rentalconnect.ui.components.NavItem
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.harsh.rentalconnect.ui.models.TenantOwnerInfo
import com.harsh.rentalconnect.ui.models.TenantPropertyDetail
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.Divider
import com.harsh.rentalconnect.ui.theme.IconTintGreen
import com.harsh.rentalconnect.ui.theme.OnPrimaryContainer
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OnVacant
import com.harsh.rentalconnect.ui.theme.OutlineSubtle
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.PrimaryContainer
import com.harsh.rentalconnect.ui.theme.PrimaryMid
import com.harsh.rentalconnect.ui.theme.PrimarySubtle
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import com.harsh.rentalconnect.ui.theme.SurfaceMuted
import com.harsh.rentalconnect.ui.theme.VacantContainer
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.cd_back
import rentalconnect.shared.generated.resources.nav_home
import rentalconnect.shared.generated.resources.nav_profile
import rentalconnect.shared.generated.resources.nav_property
import rentalconnect.shared.generated.resources.property_detail_house_number_label
import rentalconnect.shared.generated.resources.property_detail_tenant_contact
import rentalconnect.shared.generated.resources.property_detail_tenant_image_placeholder
import rentalconnect.shared.generated.resources.property_detail_tenant_owner_section
import rentalconnect.shared.generated.resources.property_detail_tenant_title
import rentalconnect.shared.generated.resources.property_detail_tenant_view_map
import rentalconnect.shared.generated.resources.property_detail_tenant_your_home
import rentalconnect.shared.generated.resources.property_detail_type_label

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyDetailTenantScreen(
    property: TenantPropertyDetail,
    owner: TenantOwnerInfo,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onViewOnMap: () -> Unit,
    onContactOwner: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.property_detail_tenant_title),
                            style = RentalConnectTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = OnSurface,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_back),
                            tint = OnSurface,
                        )
                    }
                },
                // Invisible trailing icon to keep title visually centered
                actions = {
                    Box(modifier = Modifier.size(AppSize.iconButtonHit))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                ),
            )
        },
        bottomBar = {
            BottomNavBar(
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
            // ----------------------------------------------------------------
            // Image carousel placeholder
            // ----------------------------------------------------------------
            if (property.photoUrls.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppSize.propertyImageLg)
                        .background(PrimarySubtle),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.property_detail_tenant_image_placeholder),
                        color = Primary,
                        style = RentalConnectTheme.typography.bodyMedium,
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = AppSpacing.xl),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
                ) {
                    itemsIndexed(property.photoUrls) { index, photoUrl ->
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Rental photo ${index + 1}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(AppSize.propertyImageLg)
                                .clip(RoundedCornerShape(AppRadius.md))
                                .background(PrimarySubtle),
                        )
                    }
                }
            }

            // Pagination dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppSpacing.sm),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(maxOf(property.photoUrls.size, 1)) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (index == 0) AppSize.dotActive else AppSize.dotInactive)
                            .clip(CircleShape)
                            .background(if (index == 0) Primary else OutlineSubtle),
                    )
                }
            }

            // ----------------------------------------------------------------
            // Property name and address
            // ----------------------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.xl)
                    .padding(top = AppSpacing.xl),
            ) {
                Text(
                    text = property.propertyName,
                    style = RentalConnectTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Text(
                    text = property.address,
                    style = RentalConnectTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(AppSpacing.md))

                // Chip/pill badges
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                    PropertyChip(
                        label = stringResource(Res.string.property_detail_tenant_your_home),
                        backgroundColor = VacantContainer,
                        textColor = OnVacant,
                    )
                    PropertyChip(
                        label = property.flatNumber,
                        backgroundColor = SurfaceMuted,
                        textColor = OnSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = AppSpacing.xl),
                color = Divider,
            )

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // ----------------------------------------------------------------
            // Info rows
            // ----------------------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.xl),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                InfoRow(
                    iconColor = PrimaryMid,
                    icon = Icons.Outlined.Home,
                    labelText = stringResource(Res.string.property_detail_type_label),
                    valueText = property.type,
                )
                InfoRow(
                    iconColor = IconTintGreen,
                    icon = Icons.Outlined.LocationOn,
                    labelText = stringResource(Res.string.property_detail_house_number_label),
                    valueText = property.houseNumber,
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // ----------------------------------------------------------------
            // Map placeholder
            // ----------------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.xl)
                    .height(AppSize.mapPreview)
                    .clip(RoundedCornerShape(AppRadius.md))
                    .background(PrimaryContainer)
                    .clickable(onClick = onViewOnMap),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.property_detail_tenant_view_map),
                    color = Primary,
                    style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ----------------------------------------------------------------
            // Your Owner section
            // ----------------------------------------------------------------
            Text(
                text = stringResource(Res.string.property_detail_tenant_owner_section),
                style = RentalConnectTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                ),
                color = OnSurfaceVariant,
                modifier = Modifier.padding(horizontal = AppSpacing.xl),
            )

            Spacer(modifier = Modifier.height(AppSpacing.md))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.xl),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryMid),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = owner.initials,
                        color = OnPrimaryContainer,
                        style = RentalConnectTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Spacer(modifier = Modifier.width(AppSpacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = owner.name,
                        style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = OnSurface,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = owner.phone,
                        style = RentalConnectTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                    )
                }

                Text(
                    text = stringResource(Res.string.property_detail_tenant_contact),
                    color = Primary,
                    style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.clickable(onClick = onContactOwner),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ---------------------------------------------------------------------------
// Sub-composables
// ---------------------------------------------------------------------------

@Composable
private fun PropertyChip(
    label: String,
    backgroundColor: Color,
    textColor: Color,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(AppRadius.pill))
            .background(backgroundColor)
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs),
    ) {
        Text(
            text = label,
            color = textColor,
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
        )
    }
}

@Composable
private fun InfoRow(
    iconColor: Color,
    icon: ImageVector,
    labelText: String,
    valueText: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AppSize.avatarSm)
                .clip(RoundedCornerShape(AppRadius.sm))
                .background(iconColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.md))

        Column {
            Text(
                text = labelText,
                style = RentalConnectTheme.typography.labelSmall,
                color = OnSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valueText,
                style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = OnSurface,
            )
        }
    }
}

@Composable
private fun BottomNavBar(
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
            val selected = selectedTab == index
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
                        style = RentalConnectTheme.typography.labelSmall,
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
fun PropertyDetailTenantScreenPreview() {
    RentalConnectTheme {
        PropertyDetailTenantScreen(
            property = TenantPropertyDetail(
                propertyName = "Sunrise Apartments",
                address = "14B, MG Road, Bengaluru 560001",
                flatNumber = "Flat 2A",
                type = "2BHK Apartment",
                houseNumber = "HNO-14B",
                photoUrls = listOf("https://images.unsplash.com/photo-1460317442991-0ec209397118"),
            ),
            owner = TenantOwnerInfo(
                name = "Rajesh Kumar",
                initials = "RK",
                phone = "+91 99887 76655",
            ),
            selectedTab = 1,
            onTabSelected = {},
            onViewOnMap = {},
            onContactOwner = {},
            onBackClick = {},
        )
    }
}
