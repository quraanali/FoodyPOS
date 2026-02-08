package com.quranali.pos.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.quranali.pos.screens.Routes
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    navController: NavController,
) {
    val viewModel = koinViewModel<SplashViewModel>()

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
        ProgressScreen()
    }
}

@Composable
fun ProgressScreen() {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        val slowAnimation = launch {
            progress.animateTo(
                targetValue = 0.5f,
                animationSpec = tween(durationMillis = 3000, easing = LinearEasing)
            )
        }
        slowAnimation.join()
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(text = if (progress.value < 1f) "Syncing... ${(progress.value * 100).toInt()}%" else "Done!")
    }
}