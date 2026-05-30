package com.harsh.rentalconnect.platform

import androidx.compose.runtime.Composable
import com.harsh.rentalconnect.domain.model.PickedPhoto

@Composable
expect fun rememberPropertyPhotoPicker(
    onPhotosPicked: (List<PickedPhoto>) -> Unit,
    onError: (String) -> Unit,
): () -> Unit
