package com.example.jetcaster.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.jetcaster.core.model.PodcastInfo
import kotlinx.coroutines.delay

@Composable
fun PodcastRecommendationTicker(
    podcasts: List<PodcastInfo>,
    onRecommend: (PodcastInfo) -> Unit
) {
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000L) // 30 segundos
            if (podcasts.isNotEmpty()) {
                onRecommend(podcasts.random())
            }
        }
    }
}
