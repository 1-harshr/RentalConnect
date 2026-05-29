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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harsh.rentalconnect.ui.models.OwnerStats
import com.harsh.rentalconnect.ui.models.PropertySummary
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.OccupiedContainer
import com.harsh.rentalconnect.ui.theme.OnOccupied
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OnVacant
import com.harsh.rentalconnect.ui.theme.PlaceholderAmber
import com.harsh.rentalconnect.ui.theme.PlaceholderBlue
import com.harsh.rentalconnect.ui.theme.PlaceholderIndigo
import com.harsh.rentalconnect.ui.theme.PlaceholderTeal
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.PrimaryContainer
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import com.harsh.rentalconnect.ui.theme.VacantContainer
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.nav_home
import rentalconnect.shared.generated.resources.nav_profile
import rentalconnect.shared.generated.resources.nav_properties
import rentalconnect.shared.generated.resources.nav_tenants
import rentalconnect.shared.generated.resources.owner_home_greeting
import rentalconnect.shared.generated.resources.owner_home_overview_section
import rentalconnect.shared.generated.resources.owner_home_properties_section
import rentalconnect.shared.generated.resources.owner_home_see_all
import rentalconnect.shared.generated.resources.owner_home_stat_properties
import rentalconnect.shared.generated.resources.owner_home_stat_tenants
import rentalconnect.shared.generated.resources.owner_home_stat_vacant
import rentalconnect.shared.generated.resources.status_occupied
import rentalconnect.shared.generated.resources.status_vacant

// ---------------------------------------------------------------------------
// Private helpers
// ---------------------------------------------------------------------------

private data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

// Light placeholder colors cycling for property cards
private val cardPlaceholderColors = listOf(
    PlaceholderBlue,
    PlaceholderTeal,
    PlaceholderIndigo,
    PlaceholderAmber,
)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@Composable
fun OwnerHomeScreen(
    userName: String,
    userInitials: String,
    stats: OwnerStats,
    properties: List<PropertySummary>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onSeeAllProperties: () -> Unit,
    onPropertyClick: (String) -> Unit,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navItems = listOf(
        NavItem(stringResource(Res.string.nav_home), Icons.Filled.Home, Icons.Outlined.Home),
        NavItem(stringResource(Res.string.nav_properties), Icons.Filled.Home, Icons.Outlined.Home),   // placeholder icon
        NavItem(stringResource(Res.string.nav_tenants), Icons.Filled.Person, Icons.Outlined.Person),
        NavItem(stringResource(Res.string.nav_profile), Icons.Filled.Person, Icons.Outlined.Person),
    )

    Scaffold(
        modifier = modifier,
        containerColor = Background,
        bottomBar = {
            OwnerBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                navItems = navItems,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = AppSpacing.lg),
        ) {
            // Header
            item {
                OwnerHeader(
                    userName = userName,
                    userInitials = userInitials,
                    onAvatarClick = onAvatarClick,
                    modifier = Modifier.padding(horizontal = AppSpacing.lg, vertical = AppSpacing.xl),
                )
            }

            // Overview section
            item {
                OverviewSection(
                    stats = stats,
                    modifier = Modifier.padding(horizontal = AppSpacing.lg),
                )
                Spacer(Modifier.height(AppSpacing.xxl))
            }

            // My Properties section header
            item {
                PropertiesSectionHeader(
                    onSeeAll = onSeeAllProperties,
                    modifier = Modifier.padding(horizontal = AppSpacing.lg),
                )
                Spacer(Modifier.height(AppSpacing.md))
            }

            // Property cards
            items(items = properties, key = { it.id }) { property ->
                val colorIndex = properties.indexOf(property) % cardPlaceholderColors.size
                PropertyCard(
                    property = property,
                    placeholderColor = cardPlaceholderColors[colorIndex],
                    onClick = { onPropertyClick(property.id) },
                    modifier = Modifier
                        .padding(horizontal = AppSpacing.lg)
                        .fillMaxWidth(),
                )
                Spacer(Modifier.height(AppSpacing.md))
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Header
// ---------------------------------------------------------------------------

@Composable
private fun OwnerHeader(
    userName: String,
    userInitials: String,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = stringResource(Res.string.owner_home_greeting),
                fontSize = 14.sp,
                color = OnSurfaceVariant,
                fontWeight = FontWeight.Normal,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "$userName 👋",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Primary)
                .clickable(onClick = onAvatarClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = userInitials,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Overview section
// ---------------------------------------------------------------------------

@Composable
private fun OverviewSection(
    stats: OwnerStats,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.owner_home_overview_section),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = OnSurfaceVariant,
            letterSpacing = 1.sp,
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StatCard(
                label = stringResource(Res.string.owner_home_stat_properties),
                count = stats.propertyCount,
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = stringResource(Res.string.owner_home_stat_tenants),
                count = stats.tenantCount,
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = stringResource(Res.string.owner_home_stat_vacant),
                count = stats.vacantCount,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(AppRadius.md),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = count.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Properties section
// ---------------------------------------------------------------------------

@Composable
private fun PropertiesSectionHeader(
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.owner_home_properties_section),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = OnSurfaceVariant,
            letterSpacing = 1.sp,
        )
        Text(
            text = stringResource(Res.string.owner_home_see_all),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Primary,
            modifier = Modifier.clickable(onClick = onSeeAll),
        )
    }
}

@Composable
private fun PropertyCard(
    property: PropertySummary,
    placeholderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(AppRadius.lg),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            // Image placeholder with status badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(placeholderColor),
            ) {
                // Status badge top-right
                StatusBadge(
                    isOccupied = property.isOccupied,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                )
            }

            // Card content
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = AppSpacing.md),
            ) {
                Text(
                    text = property.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = property.address,
                    fontSize = 13.sp,
                    color = OnSurfaceVariant,
                )
                Spacer(Modifier.height(AppSpacing.sm))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${property.tenantCount} tenant${if (property.tenantCount != 1) "s" else ""}",
                        fontSize = 13.sp,
                        color = OnSurfaceVariant,
                    )
                    Spacer(Modifier.width(AppSpacing.sm))
                    Box(
                        modifier = Modifier
                            .size(AppSpacing.xs)
                            .clip(CircleShape)
                            .background(OnSurfaceVariant),
                    )
                    Spacer(Modifier.width(AppSpacing.sm))
                    Text(
                        text = property.type,
                        fontSize = 13.sp,
                        color = OnSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    isOccupied: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isOccupied) OccupiedContainer else VacantContainer
    val textColor = if (isOccupied) OnOccupied else OnVacant
    val label = if (isOccupied) stringResource(Res.string.status_occupied) else stringResource(Res.string.status_vacant)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(AppRadius.pill))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = AppSpacing.xs),
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }
}

// ---------------------------------------------------------------------------
// Bottom navigation
// ---------------------------------------------------------------------------

@Composable
private fun OwnerBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navItems: List<NavItem>,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Surface,
        tonalElevation = 4.dp,
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
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    indicatorColor = PrimaryContainer,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
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
fun OwnerHomeScreenPreview() {
    RentalConnectTheme {
        OwnerHomeScreen(
            userName = "Rajesh Kumar",
            userInitials = "RK",
            stats = OwnerStats(
                propertyCount = 4,
                tenantCount = 7,
                vacantCount = 2,
            ),
            properties = listOf(
                PropertySummary(
                    id = "1",
                    name = "Sunshine Apartments",
                    address = "12 MG Road, Bengaluru",
                    tenantCount = 3,
                    type = "2BHK",
                    isOccupied = true,
                ),
                PropertySummary(
                    id = "2",
                    name = "Green Valley Villa",
                    address = "45 Koramangala, Bengaluru",
                    tenantCount = 0,
                    type = "3BHK",
                    isOccupied = false,
                ),
                PropertySummary(
                    id = "3",
                    name = "Lake View Studio",
                    address = "8 Indiranagar, Bengaluru",
                    tenantCount = 1,
                    type = "1BHK",
                    isOccupied = true,
                ),
            ),
            selectedTab = 0,
            onTabSelected = {},
            onSeeAllProperties = {},
            onPropertyClick = {},
            onAvatarClick = {},
        )
    }
}
