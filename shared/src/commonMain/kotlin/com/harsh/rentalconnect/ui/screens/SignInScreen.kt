package com.harsh.rentalconnect.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.harsh.rentalconnect.domain.model.AuthError
import com.harsh.rentalconnect.domain.model.AuthValidationError
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.Error
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.OutlineSubtle
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.error_backend_unavailable
import rentalconnect.shared.generated.resources.error_email_empty
import rentalconnect.shared.generated.resources.error_email_in_use
import rentalconnect.shared.generated.resources.error_email_invalid
import rentalconnect.shared.generated.resources.error_invalid_credentials
import rentalconnect.shared.generated.resources.error_network_message
import rentalconnect.shared.generated.resources.error_password_empty
import rentalconnect.shared.generated.resources.error_password_short
import rentalconnect.shared.generated.resources.error_phone_in_use
import rentalconnect.shared.generated.resources.sign_in_action
import rentalconnect.shared.generated.resources.sign_in_create_action
import rentalconnect.shared.generated.resources.sign_in_create_prompt
import rentalconnect.shared.generated.resources.sign_in_email_label
import rentalconnect.shared.generated.resources.sign_in_password_label
import rentalconnect.shared.generated.resources.sign_in_submitting
import rentalconnect.shared.generated.resources.sign_in_subtitle
import rentalconnect.shared.generated.resources.sign_in_title

@Composable
fun SignInScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: AuthValidationError? = null,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: AuthValidationError? = null,
    authError: AuthError? = null,
    isSubmitting: Boolean = false,
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AppSpacing.xxl, vertical = AppSpacing.xxxl),
    ) {
        Box(
            modifier = Modifier
                .size(AppSize.avatarMd)
                .clip(RoundedCornerShape(AppRadius.md))
                .background(Primary),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

        Text(
            text = stringResource(Res.string.sign_in_title),
            style = RentalConnectTheme.typography.headlineLarge,
            color = OnSurface,
        )

        Spacer(modifier = Modifier.height(AppSpacing.sm))

        Text(
            text = stringResource(Res.string.sign_in_subtitle),
            style = RentalConnectTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxxl))

        Text(
            text = stringResource(Res.string.sign_in_email_label),
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("you@example.com", color = OnSurfaceVariant) },
            isError = emailError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
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
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        if (emailError != null) {
            Text(
                text = emailValidationErrorMessage(emailError),
                color = Error,
                style = RentalConnectTheme.typography.bodySmall,
                modifier = Modifier.padding(start = AppSpacing.sm, top = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = stringResource(Res.string.sign_in_password_label),
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            isError = passwordError != null,
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            trailingIcon = {
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
            },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        if (passwordError != null) {
            Text(
                text = passwordValidationErrorMessage(passwordError),
                color = Error,
                style = RentalConnectTheme.typography.bodySmall,
                modifier = Modifier.padding(start = AppSpacing.sm, top = 4.dp),
            )
        }

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
            onClick = onSignIn,
            enabled = !isSubmitting,
            shape = RoundedCornerShape(AppRadius.md),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSize.buttonHeight),
        ) {
            Text(
                text = if (isSubmitting) stringResource(Res.string.sign_in_submitting) else stringResource(Res.string.sign_in_action),
                style = RentalConnectTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.sign_in_create_prompt) + " ",
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
            Text(
                text = stringResource(Res.string.sign_in_create_action),
                style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Primary,
                modifier = Modifier.clickable(onClick = onCreateAccount),
            )
        }
    }
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
private fun authErrorMessage(error: AuthError): String = when (error) {
    AuthError.INVALID_CREDENTIALS -> stringResource(Res.string.error_invalid_credentials)
    AuthError.EMAIL_ALREADY_IN_USE -> stringResource(Res.string.error_email_in_use)
    AuthError.PHONE_ALREADY_IN_USE -> stringResource(Res.string.error_phone_in_use)
    AuthError.NETWORK -> stringResource(Res.string.error_network_message)
    AuthError.BACKEND_NOT_CONFIGURED -> stringResource(Res.string.error_backend_unavailable)
    AuthError.UNKNOWN -> stringResource(Res.string.error_backend_unavailable)
}

@Preview
@Composable
private fun SignInScreenPreview() {
    RentalConnectTheme {
        SignInScreen(
            email = "",
            onEmailChange = {},
            emailError = null,
            password = "",
            onPasswordChange = {},
            passwordError = null,
            authError = null,
            isSubmitting = false,
            onSignIn = {},
            onCreateAccount = {},
        )
    }
}
