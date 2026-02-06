package com.quranali.pos.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.quranali.pos.screens.Routes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    navController: NavController,
) {
    val viewModel = koinViewModel<SplashViewModel>()
//    viewModel.initData()

    LaunchedEffect(viewModel.isLoading) {
        if (!viewModel.isLoading) {
            navController.navigate(Routes.HOME.name) {
                popUpTo(Routes.SPLASH.name) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}