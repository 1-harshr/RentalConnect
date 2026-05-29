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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OutlineSubtle
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.PrimaryContainer
import com.harsh.rentalconnect.ui.theme.PrimarySubtle
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import com.harsh.rentalconnect.ui.theme.SurfaceMuted
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.add_property_add_photos
import rentalconnect.shared.generated.resources.add_property_address_label
import rentalconnect.shared.generated.resources.add_property_availability_label
import rentalconnect.shared.generated.resources.add_property_house_number_label
import rentalconnect.shared.generated.resources.add_property_map_label
import rentalconnect.shared.generated.resources.add_property_name_label
import rentalconnect.shared.generated.resources.add_property_save
import rentalconnect.shared.generated.resources.add_property_tap_to_pin
import rentalconnect.shared.generated.resources.add_property_title
import rentalconnect.shared.generated.resources.add_property_type_label
import rentalconnect.shared.generated.resources.add_property_type_placeholder
import rentalconnect.shared.generated.resources.cd_back
import rentalconnect.shared.generated.resources.status_occupied
import rentalconnect.shared.generated.resources.status_vacant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(
    propertyName: String,
    onPropertyNameChange: (String) -> Unit,
    houseNumber: String,
    onHouseNumberChange: (String) -> Unit,
    fullAddress: String,
    onFullAddressChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    isOccupied: Boolean,
    onAvailabilityChange: (Boolean) -> Unit,
    onAddPhotos: () -> Unit,
    onTapToPin: () -> Unit,
    onSave: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.add_property_title),
                        style = RentalConnectTheme.typography.titleLarge,
                        color = OnSurface,
                    )
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
                actions = {
                    Text(
                        text = stringResource(Res.string.add_property_save),
                        style = RentalConnectTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = Primary,
                        modifier = Modifier
                            .padding(end = AppSpacing.lg)
                            .clickable(onClick = onSave),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                ),
            )
        },
        containerColor = Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppSpacing.xxl, vertical = AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.xl),
        ) {
            // Photo upload area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppSize.photoUpload)
                    .clip(RoundedCornerShape(AppRadius.md))
                    .background(SurfaceMuted)
                    .border(
                        width = 1.5.dp,
                        color = OutlineSubtle,
                        shape = RoundedCornerShape(AppRadius.md),
                    )
                    .clickable(onClick = onAddPhotos),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.add_property_add_photos),
                    style = RentalConnectTheme.typography.bodyLarge,
                    color = OnSurfaceVariant,
                )
            }

            // Property name field
            LabeledTextField(
                label = stringResource(Res.string.add_property_name_label),
                value = propertyName,
                onValueChange = onPropertyNameChange,
                placeholder = "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
            )

            // House number field
            LabeledTextField(
                label = stringResource(Res.string.add_property_house_number_label),
                value = houseNumber,
                onValueChange = onHouseNumberChange,
                placeholder = "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next,
                ),
            )

            // Full address field
            LabeledTextField(
                label = stringResource(Res.string.add_property_address_label),
                value = fullAddress,
                onValueChange = onFullAddressChange,
                placeholder = "",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )

            // Type field
            LabeledTextField(
                label = stringResource(Res.string.add_property_type_label),
                value = type,
                onValueChange = onTypeChange,
                placeholder = stringResource(Res.string.add_property_type_placeholder),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done,
                ),
            )

            // Availability status
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                Text(
                    text = stringResource(Res.string.add_property_availability_label),
                    style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = OnSurface,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
                ) {
                    AvailabilityToggleButton(
                        label = stringResource(Res.string.status_occupied),
                        isSelected = isOccupied,
                        onClick = { onAvailabilityChange(true) },
                        modifier = Modifier.weight(1f),
                    )

                    AvailabilityToggleButton(
                        label = stringResource(Res.string.status_vacant),
                        isSelected = !isOccupied,
                        onClick = { onAvailabilityChange(false) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // Map location
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                Text(
                    text = stringResource(Res.string.add_property_map_label),
                    style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = OnSurface,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppSize.mapPreview)
                        .clip(RoundedCornerShape(AppRadius.md))
                        .background(PrimaryContainer)
                        .border(
                            width = 1.dp,
                            color = PrimarySubtle,
                            shape = RoundedCornerShape(AppRadius.md),
                        )
                        .clickable(onClick = onTapToPin),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.add_property_tap_to_pin),
                        style = RentalConnectTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = Primary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = label,
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        text = placeholder,
                        color = OnSurfaceVariant,
                    )
                }
            } else null,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(AppSpacing.sm),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AvailabilityToggleButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) Primary else Surface
    val textColor = if (isSelected) Color.White else OnSurfaceVariant
    val borderColor = if (isSelected) Primary else OutlineSubtle

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(AppSpacing.sm))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(AppSpacing.sm),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = textColor,
        )
    }
}

@Preview
@Composable
fun AddPropertyScreenPreview() {
    RentalConnectTheme {
        AddPropertyScreen(
            propertyName = "Sunrise Apartments",
            onPropertyNameChange = {},
            houseNumber = "HNO-14B",
            onHouseNumberChange = {},
            fullAddress = "14B, MG Road, Bengaluru 560001",
            onFullAddressChange = {},
            type = "",
            onTypeChange = {},
            isOccupied = true,
            onAvailabilityChange = {},
            onAddPhotos = {},
            onTapToPin = {},
            onSave = {},
            onBackClick = {},
        )
    }
}
