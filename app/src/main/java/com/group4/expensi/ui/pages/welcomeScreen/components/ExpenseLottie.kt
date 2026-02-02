package com.group4.expensi.ui.pages.welcomeScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import com.example.expensi.R

@Composable
fun ExpenseLottie(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.expenses)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(250.dp)
    )
}
