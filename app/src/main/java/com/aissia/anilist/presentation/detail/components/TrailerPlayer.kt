package com.aissia.anilist.presentation.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.aissia.anilist.domain.model.Trailer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun TrailerPlayer(trailer: Trailer?, modifier: Modifier = Modifier.Companion) {
    if (trailer == null || trailer.site.lowercase() != "youtube" || trailer.id.isBlank()) return

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val playerView = remember {
        YouTubePlayerView(context).apply {
            lifecycleOwner.lifecycle.addObserver(this)
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(trailer.id, 0f)
                }
            })
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { playerView }
    )
}