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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
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

        // Title
        Text(
            text = stringResource(Res.string.onboarding_welcome_title),
            style = RentalConnectTheme.typography.headlineLarge,
            color = OnSurface,
        )

        Spacer(modifier = Modifier.height(AppSpacing.sm))

        // Subtitle
        Text(
            text = stringResource(Res.string.onboarding_welcome_subtitle),
            style = RentalConnectTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(AppSpacing.xxxl))

        // Role section label
        Text(
            text = stringResource(Res.string.onboarding_i_am_a),
            style = RentalConnectTheme.typography.titleMedium,
            color = OnSurface,
        )

        Spacer(modifier = Modifier.height(AppSpacing.md))

        // Role cards row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            RoleCard(
                label = stringResource(Res.string.onboarding_role_owner),
                subtitle = stringResource(Res.string.onboarding_role_owner_subtitle),
                isSelected = selectedRole == "owner",
                onSelect = { onRoleSelected("owner") },
                modifier = Modifier.weight(1f),
            )

            RoleCard(
                label = stringResource(Res.string.onboarding_role_tenant),
                subtitle = stringResource(Res.string.onboarding_role_tenant_subtitle),
                isSelected = selectedRole == "tenant",
                onSelect = { onRoleSelected("tenant") },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Phone number field
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
                Text(
                    text = stringResource(Res.string.onboarding_phone_placeholder),
                    color = OnSurfaceVariant,
                )
            },
            singleLine = true,
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

        // Full name field
        Text(
            text = stringResource(Res.string.onboarding_name_label),
            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            singleLine = true,
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

        // Continue button
        Button(
            onClick = onContinue,
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
                text = stringResource(Res.string.onboarding_continue),
                style = RentalConnectTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xxl))

        // Sign in footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = OnSurfaceVariant)) {
                        append(stringResource(Res.string.onboarding_already_have_account))
                        append(" ")
                    }
                    withStyle(SpanStyle(color = Primary, fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(Res.string.onboarding_sign_in))
                    }
                },
                style = RentalConnectTheme.typography.bodyMedium,
                modifier = Modifier.clickable(onClick = onSignIn),
            )
        }
    }
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
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(AppRadius.md),
            )
            .background(backgroundColor)
            .clickable(onClick = onSelect)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
    ) {
        // Icon placeholder box
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(AppRadius.sm))
                .background(
                    if (isSelected) PrimarySubtle else SurfaceMuted,
                ),
        )

        Text(
            text = label,
            style = RentalConnectTheme.typography.titleMedium,
            color = OnSurface,
        )

        Text(
            text = subtitle,
            style = RentalConnectTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
        )
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    RentalConnectTheme {
        OnboardingScreen(
            selectedRole = "owner",
            onRoleSelected = {},
            phone = "",
            onPhoneChange = {},
            name = "Rajesh Kumar",
            onNameChange = {},
            onContinue = {},
            onSignIn = {},
        )
    }
}
