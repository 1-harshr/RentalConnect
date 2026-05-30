package com.harsh.rentalconnect

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.harsh.rentalconnect.di.AppModule
import com.harsh.rentalconnect.navigation.AppNavGraph
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme

@Composable
fun App() {
    val session by AppModule.authRepository.session.collectAsStateWithLifecycle()

    RentalConnectTheme {
        AppNavGraph(session = session)
    }
}
