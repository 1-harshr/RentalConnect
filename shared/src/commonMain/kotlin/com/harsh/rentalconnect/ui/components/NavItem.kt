package com.harsh.rentalconnect.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Describes a single bottom-navigation tab with paired filled/outlined icons
 * for selected/unselected states.
 */
data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)
