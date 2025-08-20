package org.example.recipesphere.ui.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import org.example.recipesphere.R

@Composable
actual fun LoginAnimation(modifier: Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.login_anim)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = IterateForever,
        speed = 1.0f
    )
    LottieAnimation(composition = composition, progress = { progress }, modifier = modifier)
}
