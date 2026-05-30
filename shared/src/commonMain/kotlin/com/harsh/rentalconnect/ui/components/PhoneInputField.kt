package com.harsh.rentalconnect.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.harsh.rentalconnect.ui.theme.Error
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OutlineSubtle
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface

data class CountryCode(val flag: String, val dialCode: String, val name: String)

val countryCodes = listOf(
    CountryCode("🇮🇳", "+91", "India"),
    CountryCode("🇺🇸", "+1", "United States"),
    CountryCode("🇬🇧", "+44", "United Kingdom"),
    CountryCode("🇦🇺", "+61", "Australia"),
    CountryCode("🇨🇦", "+1", "Canada"),
    CountryCode("🇸🇬", "+65", "Singapore"),
    CountryCode("🇦🇪", "+971", "UAE"),
    CountryCode("🇩🇪", "+49", "Germany"),
    CountryCode("🇫🇷", "+33", "France"),
    CountryCode("🇯🇵", "+81", "Japan"),
)

@Composable
fun PhoneInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    selectedCountryCode: CountryCode,
    onCountryCodeChange: (CountryCode) -> Unit,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Text(
        text = label,
        style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
        color = OnSurface,
    )
    Spacer(modifier = Modifier.height(6.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = if (placeholder.isNotBlank()) {
            { Text(text = placeholder, color = OnSurfaceVariant) }
        } else null,
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
        ),
        leadingIcon = {
            Box {
                Row(
                    modifier = Modifier
                        .clickable { dropdownExpanded = true }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = selectedCountryCode.flag,
                        style = RentalConnectTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = selectedCountryCode.dialCode,
                        style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = OnSurface,
                    )
                }
                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                ) {
                    countryCodes.forEach { code ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = code.flag, style = RentalConnectTheme.typography.bodyLarge)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = code.name,
                                        style = RentalConnectTheme.typography.bodyMedium,
                                        color = OnSurface,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = code.dialCode,
                                        style = RentalConnectTheme.typography.bodySmall,
                                        color = OnSurfaceVariant,
                                    )
                                }
                            },
                            onClick = {
                                onCountryCodeChange(code)
                                dropdownExpanded = false
                            },
                        )
                    }
                }
            }
        },
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                VerticalDivider(modifier = Modifier.height(24.dp), color = OutlineSubtle)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = OutlineSubtle,
            errorBorderColor = Error,
            focusedContainerColor = Surface,
            unfocusedContainerColor = Surface,
            errorContainerColor = Surface,
        ),
        modifier = modifier,
    )
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = Error,
            style = RentalConnectTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        )
    }
}
