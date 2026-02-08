package com.quranali.pos.screens.component

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.quranali.pos.R

@Composable
fun ProgressLoader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = colorResource(R.color.primary), modifier = Modifier
                .size(90.dp),
            strokeWidth = 6.dp
        )
    }
}
