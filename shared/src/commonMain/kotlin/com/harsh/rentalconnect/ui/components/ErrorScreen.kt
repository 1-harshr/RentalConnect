package com.harsh.rentalconnect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.harsh.rentalconnect.ui.theme.AppRadius
import com.harsh.rentalconnect.ui.theme.AppSize
import com.harsh.rentalconnect.ui.theme.AppSpacing
import com.harsh.rentalconnect.ui.theme.Background
import com.harsh.rentalconnect.ui.theme.OnSurfaceVariant
import com.harsh.rentalconnect.ui.theme.Primary
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import rentalconnect.shared.generated.resources.Res
import rentalconnect.shared.generated.resources.error_generic_message
import rentalconnect.shared.generated.resources.error_retry

@Composable
fun ErrorScreen(
    message: String = "",
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val displayMessage = message.ifBlank { stringResource(Res.string.error_generic_message) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
            modifier = Modifier.padding(horizontal = AppSpacing.xxxl),
        ) {
            Text(
                text = displayMessage,
                style = RentalConnectTheme.typography.bodyLarge,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(AppRadius.md),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White,
                    ),
                    modifier = Modifier.height(AppSize.buttonHeight),
                ) {
                    Text(
                        text = stringResource(Res.string.error_retry),
                        style = RentalConnectTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}
