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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harsh.rentalconnect.domain.model.ValidationError
import com.harsh.rentalconnect.ui.models.Role
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.Error
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
import rentalconnect.shared.generated.resources.error_name_empty
import rentalconnect.shared.generated.resources.error_name_invalid_format
import rentalconnect.shared.generated.resources.error_name_too_long
import rentalconnect.shared.generated.resources.error_name_too_short
import rentalconnect.shared.generated.resources.error_phone_empty
import rentalconnect.shared.generated.resources.error_phone_invalid_format
import rentalconnect.shared.generated.resources.onboarding_already_have_account
import rentalconnect.shared.generated.resources.onboarding_continue
import rentalconnect.shared.generated.resources.onboarding_i_am_a
import rentalconnect.shared.generated.resources.onboarding_name_label
import rentalconnect.shared.generated.resources.onboarding_phone_label
import rentalconnect.shared.generated.resources.onboarding_phone_placeholder
import rentalconnect.shared.generated.resources.onboarding_role_owner
import rentalconnect.shared.generated.resources.onboarding_role_owner_subtitle
import rentalconnect.shared.generated.resources.onboarding_role_tenant
import rentalconnect.shared.generated.resources.onboarding_role_tenant_subtitle
import rentalconnect.shared.generated.resources.onboarding_sign_in
import rentalconnect.shared.generated.resources.onboarding_welcome_subtitle
import rentalconnect.shared.generated.resources.onboarding_welcome_title

@Composable
fun OnboardingScreen(
    selectedRole: Role,
    onRoleSelected: (Role) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    phoneError: ValidationError? = null,
    name: String,
    onNameChange: (String) -> Unit,
    nameError: ValidationError? = null,
    canContinue: Boolean = true,
    onContinue: () -> Unit,
    onSignIn: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(scrollState)
            .padding(horizontal = AppSpacing.xxl, vertical = AppSpacing.xxxl),
    ) {
        // App logo — blue rounded-square box
        Box(
            modifier = Modifier
                .size(AppSize.avatarMd)
                .clip(RoundedCornerShape(AppRadius.md))
                .background(Primary),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

        Text(
            text = stringResource(Res.string.onboarding_welcome_title),
            style = RentalConnectTheme.typography.headlineLarge,
            color = OnSurface,
        )

        Spacer(modifier = Modifier.height(AppSpacing.sm))

        Text(
            text = stringResource(Res.string.onboarding_welcome_subtitle),
            style = RentalConnectTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxxl))

        Text(
            text = stringResource(Res.string.onboarding_i_am_a),
            style = RentalConnectTheme.typography.titleMedium,
            color = OnSurface,
        )

        Spacer(modifier = Modifier.height(AppSpacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            RoleCard(
                label = stringResource(Res.string.onboarding_role_owner),
                subtitle = stringResource(Res.string.onboarding_role_owner_subtitle),
                isSelected = selectedRole == Role.Owner,
                onSelect = { onRoleSelected(Role.Owner) },
                modifier = Modifier.weight(1f),
            )
            RoleCard(
                label = stringResource(Res.string.onboarding_role_tenant),
                subtitle = stringResource(Res.string.onboarding_role_tenant_subtitle),
                isSelected = selectedRole == Role.Tenant,
                onSelect = { onRoleSelected(Role.Tenant) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Phone field ───────────────────────────────────────────────────────
        Text(
            text = stringResource(Res.string.onboarding_phone_label),
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            placeholder = {
                Text(text = stringResource(Res.string.onboarding_phone_placeholder), color = OnSurfaceVariant)
            },
            isError = phoneError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                errorBorderColor = Error,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                errorContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        if (phoneError != null) {
            Text(
                text = phoneErrorMessage(phoneError),
                color = Error,
                style = RentalConnectTheme.typography.bodySmall,
                modifier = Modifier.padding(start = AppSpacing.sm, top = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        // ── Name field ────────────────────────────────────────────────────────
        Text(
            text = stringResource(Res.string.onboarding_name_label),
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            isError = nameError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                errorBorderColor = Error,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
                errorContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        if (nameError != null) {
            Text(
                text = nameErrorMessage(nameError),
                color = Error,
                style = RentalConnectTheme.typography.bodySmall,
                modifier = Modifier.padding(start = AppSpacing.sm, top = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xxxl))

        // ── Continue button ───────────────────────────────────────────────────
        Button(
            onClick = onContinue,
            enabled = canContinue,
            shape = RoundedCornerShape(AppRadius.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White,
                disabledContainerColor = Primary.copy(alpha = 0.4f),
                disabledContentColor = Color.White.copy(alpha = 0.7f),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSize.buttonHeight),
        ) {
            Text(
                text = stringResource(Res.string.onboarding_continue),
                style = RentalConnectTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

        // ── Sign in footer ────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.onboarding_already_have_account) + " ",
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
            Text(
                text = stringResource(Res.string.onboarding_sign_in),
                style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Primary,
                modifier = Modifier.clickable(onClick = onSignIn),
            )
        }
    }
}

@Composable
private fun nameErrorMessage(error: ValidationError): String = when (error) {
    ValidationError.EMPTY -> stringResource(Res.string.error_name_empty)
    ValidationError.TOO_SHORT -> stringResource(Res.string.error_name_too_short)
    ValidationError.TOO_LONG -> stringResource(Res.string.error_name_too_long)
    ValidationError.INVALID_FORMAT -> stringResource(Res.string.error_name_invalid_format)
}

@Composable
private fun phoneErrorMessage(error: ValidationError): String = when (error) {
    ValidationError.EMPTY -> stringResource(Res.string.error_phone_empty)
    ValidationError.TOO_SHORT,
    ValidationError.TOO_LONG,
    ValidationError.INVALID_FORMAT -> stringResource(Res.string.error_phone_invalid_format)
}

@Composable
private fun RoleCard(
    label: String,
    subtitle: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) Primary else OutlineSubtle
    val backgroundColor = if (isSelected) PrimaryContainer else Surface
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppRadius.md))
            .border(width = borderWidth, color = borderColor, shape = RoundedCornerShape(AppRadius.md))
            .background(backgroundColor)
            .clickable(onClick = onSelect)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(AppRadius.sm))
                .background(if (isSelected) PrimarySubtle else SurfaceMuted),
        )
        Text(text = label, style = RentalConnectTheme.typography.titleMedium, color = OnSurface)
        Text(text = subtitle, style = RentalConnectTheme.typography.bodyMedium, color = OnSurfaceVariant)
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    RentalConnectTheme {
        OnboardingScreen(
            selectedRole = Role.Owner,
            onRoleSelected = {},
            phone = "123",
            onPhoneChange = {},
            phoneError = ValidationError.INVALID_FORMAT,
            name = "",
            onNameChange = {},
            nameError = ValidationError.EMPTY,
            canContinue = false,
            onContinue = {},
            onSignIn = {},
        )
    }
}
