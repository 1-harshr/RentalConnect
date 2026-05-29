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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import com.harsh.rentalconnect.ui.theme.Surface

@Composable
fun SignInScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit,
) {
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
            text = "Welcome back",
            style = RentalConnectTheme.typography.headlineLarge,
            color = OnSurface,
        )

        Spacer(modifier = Modifier.height(AppSpacing.sm))

        Text(
            text = "Sign in to continue",
            style = RentalConnectTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxxl))

        Text(
            text = "Email",
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("you@example.com", color = OnSurfaceVariant) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Text(
            text = "Password",
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineSubtle,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxxl))

        Button(
            onClick = onSignIn,
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
                text = "Sign in",
                style = RentalConnectTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Don't have an account? ",
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
            Text(
                text = "Create one",
                style = RentalConnectTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Primary,
                modifier = Modifier.clickable(onClick = onCreateAccount),
            )
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    RentalConnectTheme {
        SignInScreen(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            onSignIn = {},
            onCreateAccount = {},
        )
    }
}
