package com.group4.expensi.ui.pages.welcomeScreen.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensi.R

@Composable
fun TopScreenAnimation(modifier: Modifier = Modifier, height: Dp = 350.dp) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.welcome_screen)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(height)
    )
}