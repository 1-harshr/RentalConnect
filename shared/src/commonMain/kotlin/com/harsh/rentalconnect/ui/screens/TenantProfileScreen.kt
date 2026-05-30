package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.harsh.rentalconnect.ui.models.TenantDetail
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.Divider
import com.harsh.rentalconnect.ui.theme.Error
import com.harsh.rentalconnect.ui.theme.IconTintAmber
import com.harsh.rentalconnect.ui.theme.IconTintGreen
import com.harsh.rentalconnect.ui.theme.IconTintPurple
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OutlineSubtle
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.PrimarySubtle
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.cd_back
import rentalconnect.shared.generated.resources.nav_home
import rentalconnect.shared.generated.resources.nav_profile
import rentalconnect.shared.generated.resources.nav_properties
import rentalconnect.shared.generated.resources.nav_tenants
import rentalconnect.shared.generated.resources.tenant_profile_aadhar_label
import rentalconnect.shared.generated.resources.tenant_profile_assigned_section
import rentalconnect.shared.generated.resources.tenant_profile_call_button
import rentalconnect.shared.generated.resources.tenant_profile_hometown_label
import rentalconnect.shared.generated.resources.tenant_profile_phone_label
import rentalconnect.shared.generated.resources.tenant_profile_remove
import rentalconnect.shared.generated.resources.tenant_profile_remove_button
import rentalconnect.shared.generated.resources.tenant_profile_since_label
import rentalconnect.shared.generated.resources.tenant_profile_title

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@Composable
fun TenantProfileScreen(
    tenant: TenantDetail,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onRemove: () -> Unit,
    onCallPhone: () -> Unit,
    onCallTenant: () -> Unit,
    onRemoveTenant: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TenantProfileTopBar(onRemove = onRemove, onBackClick = onBackClick)
        },
        bottomBar = {
            TenantProfileBottomNav(
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
            // Tenant header
            TenantHeader(tenant = tenant)

            HorizontalDivider(color = Divider)

            // Info rows
            InfoRow(
                iconColor = PrimarySubtle,
                icon = Icons.Outlined.Phone,
                label = stringResource(Res.string.tenant_profile_phone_label),
                value = tenant.phone,
                valueColor = Primary,
                onValueClick = onCallPhone,
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = AppSize.avatarLg),
                color = Divider,
            )

            InfoRow(
                iconColor = IconTintAmber,
                icon = Icons.Outlined.LocationOn,
                label = stringResource(Res.string.tenant_profile_hometown_label),
                value = tenant.hometown,
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = AppSize.avatarLg),
                color = Divider,
            )

            InfoRow(
                iconColor = IconTintPurple,
                icon = Icons.Outlined.AccountCircle,
                label = stringResource(Res.string.tenant_profile_aadhar_label),
                value = tenant.aadharId,
            )

            HorizontalDivider(
                modifier = Modifier.padding(start = AppSize.avatarLg),
                color = Divider,
            )

            InfoRow(
                iconColor = IconTintGreen,
                icon = Icons.Outlined.Home,
                label = stringResource(Res.string.tenant_profile_since_label),
                value = tenant.tenantSince,
            )

            HorizontalDivider(color = Divider)

            Spacer(modifier = Modifier.height(AppSpacing.xl))

            // Assigned property section
            Text(
                text = stringResource(Res.string.tenant_profile_assigned_section),
                style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = OnSurfaceVariant,
                modifier = Modifier.padding(horizontal = AppSpacing.xl),
            )

            Spacer(modifier = Modifier.height(AppSpacing.md))

            AssignedPropertyCard(
                propertyName = tenant.assignedPropertyName,
                propertyAddress = tenant.assignedPropertyAddress,
                modifier = Modifier.padding(horizontal = AppSpacing.xl),
            )

            Spacer(modifier = Modifier.height(AppSpacing.xxl))

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.xl),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
            ) {
                OutlinedButton(
                    onClick = onCallTenant,
                    shape = RoundedCornerShape(AppRadius.md),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
                    border = BorderStroke(1.dp, Primary),
                    modifier = Modifier
                        .weight(1f)
                        .height(AppSize.avatarMd),
                ) {
                    Text(
                        text = stringResource(Res.string.tenant_profile_call_button),
                        style = RentalConnectTheme.typography.titleMedium,
                    )
                }

                OutlinedButton(
                    onClick = onRemoveTenant,
                    shape = RoundedCornerShape(AppRadius.md),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Error),
                    border = BorderStroke(1.dp, Error),
                    modifier = Modifier
                        .weight(1f)
                        .height(AppSize.avatarMd),
                ) {
                    Text(
                        text = stringResource(Res.string.tenant_profile_remove_button),
                        style = RentalConnectTheme.typography.titleMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.xxl))
        }
    }
}

// ---------------------------------------------------------------------------
// Top bar
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TenantProfileTopBar(onRemove: () -> Unit, onBackClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface)
                .padding(end = AppSpacing.xl),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.cd_back),
                    tint = OnSurface,
                )
            }

            Text(
                text = stringResource(Res.string.tenant_profile_title),
                style = RentalConnectTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                ),
                color = OnSurface,
            )

            Text(
                text = stringResource(Res.string.tenant_profile_remove),
                style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Error,
                modifier = Modifier.clickable(onClick = onRemove),
            )
        }

        HorizontalDivider(color = Divider)
    }
}

// ---------------------------------------------------------------------------
// Tenant header
// ---------------------------------------------------------------------------

@Composable
private fun TenantHeader(tenant: TenantDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = AppSpacing.xl, vertical = AppSpacing.xxl),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar circle
        Box(
            modifier = Modifier
                .size(AppSize.avatarLg)
                .clip(CircleShape)
                .background(PrimarySubtle),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = tenant.initials,
                style = RentalConnectTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                ),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.lg))

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Text(
                text = tenant.name,
                style = RentalConnectTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )

            Text(
                text = "Tenant · ${tenant.propertyName}, ${tenant.flatNumber}",
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Info row
// ---------------------------------------------------------------------------

@Composable
private fun InfoRow(
    iconColor: Color,
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = OnSurface,
    onValueClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = AppSpacing.xl, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
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

        Spacer(modifier = Modifier.width(AppSpacing.lg))

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = RentalConnectTheme.typography.labelMedium,
                color = OnSurfaceVariant,
            )

            val textModifier = if (onValueClick != null) {
                Modifier.clickable(onClick = onValueClick)
            } else {
                Modifier
            }

            Text(
                text = value,
                style = RentalConnectTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = valueColor,
                modifier = textModifier,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Assigned property card
// ---------------------------------------------------------------------------

@Composable
private fun AssignedPropertyCard(
    propertyName: String,
    propertyAddress: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.md))
            .background(Surface)
            .padding(AppSpacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Divider),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Home,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Text(
                text = propertyName,
                style = RentalConnectTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )

            Text(
                text = propertyAddress,
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Bottom navigation
// ---------------------------------------------------------------------------

@Composable
private fun TenantProfileBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    val navItems = listOf(
        NavItem(stringResource(Res.string.nav_home), Icons.Filled.Home, Icons.Outlined.Home),
        NavItem(stringResource(Res.string.nav_properties), Icons.Filled.LocationOn, Icons.Outlined.LocationOn),
        NavItem(stringResource(Res.string.nav_tenants), Icons.Filled.Person, Icons.Outlined.Person),
        NavItem(stringResource(Res.string.nav_profile), Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle),
    )

    NavigationBar(
        containerColor = Surface,
        tonalElevation = 0.dp,
    ) {
        navItems.forEachIndexed { index, item ->
            val isSelected = index == selectedTab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp),
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = RentalConnectTheme.typography.labelMedium,
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
fun TenantProfileScreenPreview() {
    RentalConnectTheme {
        TenantProfileScreen(
            tenant = TenantDetail(
                id = "tenant_00" +
                        "1",
                name = "Arjun Khanna",
                initials = "AK",
                propertyName = "Sunrise Apartments",
                flatNumber = "Flat 2A",
                phone = "+91 98765 43210",
                hometown = "Chandigarh, Punjab",
                aadharId = "XXXX XXXX 4321",
                tenantSince = "January 12, 2024",
                assignedPropertyName = "Sunrise Apartments",
                assignedPropertyAddress = "14B, MG Road, Bengaluru",
            ),
            selectedTab = 2,
            onTabSelected = {},
            onRemove = {},
            onCallPhone = {},
            onCallTenant = {},
            onRemoveTenant = {},
            onBackClick = {},
        )
    }
}
