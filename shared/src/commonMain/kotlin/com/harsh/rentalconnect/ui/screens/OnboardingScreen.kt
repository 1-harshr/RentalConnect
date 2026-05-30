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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harsh.rentalconnect.ui.components.CountryCode
import com.harsh.rentalconnect.ui.components.PhoneInputField
import com.harsh.rentalconnect.ui.components.countryCodes
import com.harsh.rentalconnect.domain.model.AuthError
import com.harsh.rentalconnect.domain.model.AuthValidationError
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
import rentalconnect.shared.generated.resources.error_aadhar_invalid
import rentalconnect.shared.generated.resources.error_aadhar_required
import rentalconnect.shared.generated.resources.error_backend_unavailable
import rentalconnect.shared.generated.resources.error_confirm_password_mismatch
import rentalconnect.shared.generated.resources.error_email_empty
import rentalconnect.shared.generated.resources.error_email_in_use
import rentalconnect.shared.generated.resources.error_email_invalid
import rentalconnect.shared.generated.resources.error_generic_required
import rentalconnect.shared.generated.resources.error_name_empty
import rentalconnect.shared.generated.resources.error_name_invalid_format
import rentalconnect.shared.generated.resources.error_name_too_long
import rentalconnect.shared.generated.resources.error_name_too_short
import rentalconnect.shared.generated.resources.error_network_message
import rentalconnect.shared.generated.resources.error_password_empty
import rentalconnect.shared.generated.resources.error_password_short
import rentalconnect.shared.generated.resources.error_phone_empty
import rentalconnect.shared.generated.resources.error_phone_in_use
import rentalconnect.shared.generated.resources.error_phone_invalid_format
import rentalconnect.shared.generated.resources.onboarding_already_have_account
import rentalconnect.shared.generated.resources.onboarding_confirm_password_label
import rentalconnect.shared.generated.resources.onboarding_continue
import rentalconnect.shared.generated.resources.onboarding_creating_account
import rentalconnect.shared.generated.resources.onboarding_hometown_label
import rentalconnect.shared.generated.resources.onboarding_email_label
import rentalconnect.shared.generated.resources.onboarding_email_placeholder
import rentalconnect.shared.generated.resources.onboarding_i_am_a
import rentalconnect.shared.generated.resources.onboarding_name_label
import rentalconnect.shared.generated.resources.onboarding_password_label
import rentalconnect.shared.generated.resources.onboarding_aadhar_label
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
    selectedCountryCode: CountryCode = countryCodes.first(),
    onCountryCodeChange: (CountryCode) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    nameError: ValidationError? = null,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: AuthValidationError? = null,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: AuthValidationError? = null,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    confirmPasswordError: AuthValidationError? = null,
    hometown: String,
    onHometownChange: (String) -> Unit,
    hometownError: ValidationError? = null,
    aadharId: String,
    onAadharChange: (String) -> Unit,
    aadharError: AuthValidationError? = null,
    authError: AuthError? = null,
    isSubmitting: Boolean = false,
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
        Box(
            modifier = Modifier
                .size(AppSize.avatarMd)
                .clip(RoundedCornerShape(AppRadius.md))
                .background(Primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Apartment,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp),
            )
        }

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
                icon = Icons.Outlined.Business,
                modifier = Modifier.weight(1f),
            )
            RoleCard(
                label = stringResource(Res.string.onboarding_role_tenant),
                subtitle = stringResource(Res.string.onboarding_role_tenant_subtitle),
                isSelected = selectedRole == Role.Tenant,
                onSelect = { onRoleSelected(Role.Tenant) },
                icon = Icons.Outlined.Person,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        PhoneInputField(
            label = stringResource(Res.string.onboarding_phone_label),
            value = phone,
            onValueChange = onPhoneChange,
            selectedCountryCode = selectedCountryCode,
            onCountryCodeChange = onCountryCodeChange,
            placeholder = stringResource(Res.string.onboarding_phone_placeholder),
            isError = phoneError != null,
            errorMessage = phoneError?.let { phoneErrorMessage(it) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        AuthTextField(
            label = stringResource(Res.string.onboarding_name_label),
            value = name,
            onValueChange = onNameChange,
            isError = nameError != null,
            errorMessage = nameError?.let { nameErrorMessage(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        AuthTextField(
            label = stringResource(Res.string.onboarding_email_label),
            value = email,
            onValueChange = onEmailChange,
            placeholder = stringResource(Res.string.onboarding_email_placeholder),
            isError = emailError != null,
            errorMessage = emailError?.let { emailValidationErrorMessage(it) },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VerticalDivider(modifier = Modifier.height(24.dp), color = OutlineSubtle)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        AuthTextField(
            label = stringResource(Res.string.onboarding_password_label),
            value = password,
            onValueChange = onPasswordChange,
            isError = passwordError != null,
            errorMessage = passwordError?.let { passwordValidationErrorMessage(it) },
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        AuthTextField(
            label = stringResource(Res.string.onboarding_confirm_password_label),
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            isError = confirmPasswordError != null,
            errorMessage = confirmPasswordError?.let { confirmPasswordValidationErrorMessage(it) },
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        AuthTextField(
            label = stringResource(Res.string.onboarding_hometown_label),
            value = hometown,
            onValueChange = onHometownChange,
            isError = hometownError != null,
            errorMessage = hometownError?.let { hometownErrorMessage(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        AuthTextField(
            label = stringResource(Res.string.onboarding_aadhar_label),
            value = aadharId,
            onValueChange = onAadharChange,
            isError = aadharError != null,
            errorMessage = aadharError?.let { aadharValidationErrorMessage(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
        )

        if (authError != null) {
            Text(
                text = authErrorMessage(authError),
                color = Error,
                style = RentalConnectTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = AppSpacing.lg),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xxxl))

        Button(
            onClick = onContinue,
            enabled = canContinue && !isSubmitting,
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
                text = if (isSubmitting) stringResource(Res.string.onboarding_creating_account) else stringResource(Res.string.onboarding_continue),
                style = RentalConnectTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

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
private fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None

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
        } else {
            null
        },
        isError = isError,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        trailingIcon = when {
            isPassword -> {
                {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VerticalDivider(modifier = Modifier.height(24.dp), color = OutlineSubtle)
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = OnSurfaceVariant,
                            )
                        }
                    }
                }
            }
            trailingIcon != null -> trailingIcon
            else -> null
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
        modifier = Modifier.fillMaxWidth(),
    )
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = Error,
            style = RentalConnectTheme.typography.bodySmall,
            modifier = Modifier.padding(start = AppSpacing.sm, top = 4.dp),
        )
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
private fun hometownErrorMessage(error: ValidationError): String = when (error) {
    ValidationError.EMPTY,
    ValidationError.TOO_SHORT,
    ValidationError.TOO_LONG,
    ValidationError.INVALID_FORMAT -> stringResource(Res.string.error_generic_required)
}

@Composable
private fun emailValidationErrorMessage(error: AuthValidationError): String = when (error) {
    AuthValidationError.EMPTY -> stringResource(Res.string.error_email_empty)
    AuthValidationError.INVALID_FORMAT -> stringResource(Res.string.error_email_invalid)
    AuthValidationError.TOO_SHORT,
    AuthValidationError.MISMATCH -> stringResource(Res.string.error_email_invalid)
}

@Composable
private fun passwordValidationErrorMessage(error: AuthValidationError): String = when (error) {
    AuthValidationError.EMPTY -> stringResource(Res.string.error_password_empty)
    AuthValidationError.TOO_SHORT -> stringResource(Res.string.error_password_short)
    AuthValidationError.INVALID_FORMAT,
    AuthValidationError.MISMATCH -> stringResource(Res.string.error_password_short)
}

@Composable
private fun confirmPasswordValidationErrorMessage(error: AuthValidationError): String = when (error) {
    AuthValidationError.MISMATCH -> stringResource(Res.string.error_confirm_password_mismatch)
    AuthValidationError.EMPTY -> stringResource(Res.string.error_password_empty)
    AuthValidationError.INVALID_FORMAT,
    AuthValidationError.TOO_SHORT -> stringResource(Res.string.error_confirm_password_mismatch)
}

@Composable
private fun aadharValidationErrorMessage(error: AuthValidationError): String = when (error) {
    AuthValidationError.EMPTY -> stringResource(Res.string.error_aadhar_required)
    AuthValidationError.INVALID_FORMAT,
    AuthValidationError.TOO_SHORT,
    AuthValidationError.MISMATCH -> stringResource(Res.string.error_aadhar_invalid)
}

@Composable
private fun authErrorMessage(error: AuthError): String = when (error) {
    AuthError.EMAIL_ALREADY_IN_USE -> stringResource(Res.string.error_email_in_use)
    AuthError.PHONE_ALREADY_IN_USE -> stringResource(Res.string.error_phone_in_use)
    AuthError.NETWORK -> stringResource(Res.string.error_network_message)
    AuthError.BACKEND_NOT_CONFIGURED -> stringResource(Res.string.error_backend_unavailable)
    AuthError.INVALID_CREDENTIALS,
    AuthError.UNKNOWN -> stringResource(Res.string.error_backend_unavailable)
}

@Composable
private fun RoleCard(
    label: String,
    subtitle: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    icon: ImageVector,
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
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Primary else OnSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
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
            onCountryCodeChange = {},
            name = "",
            onNameChange = {},
            nameError = ValidationError.EMPTY,
            email = "demo@",
            onEmailChange = {},
            emailError = AuthValidationError.INVALID_FORMAT,
            password = "123",
            onPasswordChange = {},
            passwordError = AuthValidationError.TOO_SHORT,
            confirmPassword = "1234",
            onConfirmPasswordChange = {},
            confirmPasswordError = AuthValidationError.MISMATCH,
            hometown = "",
            onHometownChange = {},
            hometownError = ValidationError.EMPTY,
            aadharId = "123",
            onAadharChange = {},
            aadharError = AuthValidationError.INVALID_FORMAT,
            authError = null,
            isSubmitting = false,
            canContinue = false,
            onContinue = {},
            onSignIn = {},
        )
    }
}
