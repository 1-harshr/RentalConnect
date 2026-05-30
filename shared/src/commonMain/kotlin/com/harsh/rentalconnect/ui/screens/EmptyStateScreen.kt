package com.harsh.rentalconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.OnSurface
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme

@Composable
fun EmptyStateScreen(
    title: String,
    body: String,
    primaryActionLabel: String,
    onPrimaryAction: () -> Unit,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(AppSpacing.xxxl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = RentalConnectTheme.typography.headlineMedium,
            color = OnSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = body,
            style = RentalConnectTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = AppSpacing.md, bottom = AppSpacing.xxl),
        )
        Button(
            onClick = onPrimaryAction,
            shape = RoundedCornerShape(AppRadius.md),
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(primaryActionLabel)
        }
        if (secondaryActionLabel != null && onSecondaryAction != null) {
            OutlinedButton(
                onClick = onSecondaryAction,
                shape = RoundedCornerShape(AppRadius.md),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppSpacing.md),
            ) {
                Text(secondaryActionLabel)
            }
        }
    }
}
