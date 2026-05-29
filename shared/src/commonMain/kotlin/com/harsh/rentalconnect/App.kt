package com.harsh.rentalconnect

import androidx.compose.runtime.Composable
import com.harsh.rentalconnect.navigation.AppNavGraph
import com.harsh.rentalconnect.ui.theme.RentalConnectTheme

@Composable
fun App() {
    RentalConnectTheme {
        AppNavGraph()
    }
}
