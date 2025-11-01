@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.jetcaster.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.scaleOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.layout.DisplayFeature
import com.example.jetcaster.R
import com.example.jetcaster.ui.components.TikTokNotificationBanner
import com.example.jetcaster.ui.home.MainScreen
import com.example.jetcaster.ui.player.PlayerScreen
import kotlinx.coroutines.delay

@Composable
fun JetcasterApp(
    displayFeatures: List<DisplayFeature>,
    appState: JetcasterAppState = rememberJetcasterAppState()
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    // ✅ Estado global para mensaje de recomendación
    val notificationState = remember { mutableStateOf<String?>(null) }

    // ✅ Mensajes sugeridos
    val messages = listOf(
        "🔥 Nuevo podcast recomendado para ti",
        "🎧 Descubre un episodio que te va a encantar",
        "🚀 Explora una nueva historia auditiva",
        "✨ Recomendación especial para tus oídos",
        "🎙 Tu próximo podcast favorito te espera"
    )

    // ✅ Generar recomendación cada 15 segundos
    LaunchedEffect(Unit) {
        while (true) {
            delay(15000)
            notificationState.value = messages.random()
        }
    }

    if (appState.isOnline) {
        SharedTransitionLayout {

            // ✅ Banner flotante global
            val homeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel<com.example.jetcaster.ui.home.HomeViewModel>()

            TikTokNotificationBanner(
                message = notificationState.value,
                onDismiss = { notificationState.value = null },
                onClick = {
                    notificationState.value = null

                    val podcasts = homeViewModel.state.value.featuredPodcasts

                    if (podcasts.isNotEmpty()) {
                        val randomPodcast = podcasts.random()
                        appState.navigateToRandomPodcast(randomPodcast.uri)
                    }
                }
            )

            CompositionLocalProvider(
                LocalSharedTransitionScope provides this,
            ) {
                NavHost(
                    navController = appState.navController,
                    startDestination = Screen.Home.route,
                    popExitTransition = { scaleOut(targetScale = 0.9f) },
                    popEnterTransition = { EnterTransition.None },
                ) {
                    composable(Screen.Home.route) { backStackEntry ->
                        CompositionLocalProvider(
                            LocalAnimatedVisibilityScope provides this,
                        ) {
                            MainScreen(
                                windowSizeClass = adaptiveInfo.windowSizeClass,
                                navigateToPlayer = { episode ->
                                    appState.navigateToPlayer(episode.uri, backStackEntry)
                                }
                            )
                        }
                    }

                    composable(Screen.Player.route) {
                        CompositionLocalProvider(
                            LocalAnimatedVisibilityScope provides this,
                        ) {
                            PlayerScreen(
                                windowSizeClass = adaptiveInfo.windowSizeClass,
                                displayFeatures = displayFeatures,
                                onBackPress = appState::navigateBack,
                            )
                        }
                    }
                }
            }
        }
    } else {
        OfflineDialog { appState.refreshOnline() }
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.connection_error_title)) },
        text = { Text(text = stringResource(R.string.connection_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.retry_label))
            }
        },
    )
}

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
