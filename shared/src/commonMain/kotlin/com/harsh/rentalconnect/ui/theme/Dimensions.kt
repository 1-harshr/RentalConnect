package com.harsh.rentalconnect.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing tokens — use for padding, gap, and margin values.
 */
object AppSpacing {
    val xs   = 4.dp
    val sm   = 8.dp
    val md   = 12.dp
    val lg   = 16.dp
    val xl   = 20.dp
    val xxl  = 24.dp
    val xxxl = 32.dp
}

/**
 * Corner radius tokens.
 */
object AppRadius {
    val sm   = 8.dp
    val md   = 12.dp
    val lg   = 16.dp
    val pill = 50.dp
}

/**
 * Fixed component size tokens — avatars, icon boxes, image areas, etc.
 */
object AppSize {
    // Avatars / initials circles
    val avatarSm = 36.dp
    val avatarMd = 48.dp
    val avatarLg = 72.dp

    // Coloured icon/illustration boxes used in info rows
    val iconBox = 40.dp

    // Interactive elements
    val buttonHeight  = 52.dp
    val iconButtonHit = 48.dp   // minimum touch target

    // Media / placeholder areas
    val photoUpload        = 140.dp
    val propertyImageMd    = 180.dp
    val propertyImageLg    = 220.dp
    val mapPreview         = 150.dp

    // Pagination dots
    val dotActive   = 8.dp
    val dotInactive = 6.dp
}
