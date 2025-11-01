package com.example.jetcaster.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TikTokNotificationBanner(
    message: String?,
    onDismiss: () -> Unit,
    onClick: () -> Unit   // ✅ Added navigation callback
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(message) {
        if (message != null) {
            visible = true
            delay(3500) // visible time on screen
            visible = false
            delay(250)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -200 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -200 }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .clickable {
                    onClick()   // ✅ Navigate to random podcast
                    visible = false
                    onDismiss()
                }
                .background(Color.Black.copy(alpha = 0.90f))
                .padding(16.dp)
        ) {
            Text(
                text = message ?: "",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}
