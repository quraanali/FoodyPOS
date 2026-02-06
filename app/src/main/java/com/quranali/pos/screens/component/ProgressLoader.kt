package com.quranali.pos.screens.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressLoader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = Color.Red, modifier = Modifier
                .padding(16.dp)
                .size(60.dp),
            strokeWidth = 6.dp
        )
    }
}
