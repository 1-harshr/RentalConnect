package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.harsh.rentalconnect.domain.model.AuthUser
import com.harsh.rentalconnect.ui.theme.AppRadius
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
import rentalconnect.shared.generated.resources.add_tenant_attaching
import rentalconnect.shared.generated.resources.add_tenant_confirm
import rentalconnect.shared.generated.resources.add_tenant_hint
import rentalconnect.shared.generated.resources.add_tenant_phone_label
import rentalconnect.shared.generated.resources.add_tenant_result_title
import rentalconnect.shared.generated.resources.add_tenant_search
import rentalconnect.shared.generated.resources.add_tenant_searching
import rentalconnect.shared.generated.resources.add_tenant_title
import rentalconnect.shared.generated.resources.cd_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTenantScreen(
    phone: String,
    phoneError: String?,
    candidate: AuthUser?,
    searchError: String?,
    isSearching: Boolean,
    isConfirming: Boolean,
    onPhoneChange: (String) -> Unit,
    onSearch: () -> Unit,
    onConfirm: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.add_tenant_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(stringResource(Res.string.cd_back)) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
            )
        },
        containerColor = Background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(AppSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
        ) {
            Text(
                text = stringResource(Res.string.add_tenant_hint),
                style = RentalConnectTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                isError = phoneError != null,
                label = { Text(stringResource(Res.string.add_tenant_phone_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OutlineSubtle,
                    errorBorderColor = Error,
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            if (phoneError != null) {
                Text(phoneError, color = Error, style = RentalConnectTheme.typography.bodySmall)
            }
            Button(
                onClick = onSearch,
                enabled = !isSearching,
                shape = RoundedCornerShape(AppRadius.md),
                colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (isSearching) stringResource(Res.string.add_tenant_searching) else stringResource(Res.string.add_tenant_search))
            }
            if (searchError != null) {
                Text(searchError, color = Error, style = RentalConnectTheme.typography.bodyMedium)
            }
            if (candidate != null) {
                Card(
                    shape = RoundedCornerShape(AppRadius.md),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(AppSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                    ) {
                        Text(
                            text = stringResource(Res.string.add_tenant_result_title),
                            style = RentalConnectTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = OnSurfaceVariant,
                        )
                        Text(candidate.name, style = RentalConnectTheme.typography.titleMedium, color = OnSurface)
                        Text(candidate.phone, style = RentalConnectTheme.typography.bodyMedium, color = OnSurfaceVariant)
                        Text(candidate.hometown, style = RentalConnectTheme.typography.bodyMedium, color = OnSurfaceVariant)
                        Button(
                            onClick = onConfirm,
                            enabled = !isConfirming,
                            shape = RoundedCornerShape(AppRadius.md),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(if (isConfirming) stringResource(Res.string.add_tenant_attaching) else stringResource(Res.string.add_tenant_confirm))
                        }
                    }
                }
            }
        }
    }
}
