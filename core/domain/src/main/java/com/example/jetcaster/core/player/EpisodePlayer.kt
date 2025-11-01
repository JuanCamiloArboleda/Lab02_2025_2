/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetcaster.core.player

import androidx.compose.runtime.Immutable
import com.example.jetcaster.core.player.model.PlayerEpisode
import java.time.Duration
import kotlinx.coroutines.flow.StateFlow

val DefaultPlaybackSpeed = Duration.ofSeconds(1)

@Immutable
data class EpisodePlayerState(
    val currentEpisode: PlayerEpisode? = null,
    val queue: List<PlayerEpisode> = emptyList(),
    val playbackSpeed: Duration = DefaultPlaybackSpeed,
    val isPlaying: Boolean = false,
    val timeElapsed: Duration = Duration.ZERO,
)

/**
 * Interface definition for an episode player defining high-level functions such as queuing
 * episodes, playing an episode, pausing, seeking, etc.
 */
interface EpisodePlayer {

    val playerState: StateFlow<EpisodePlayerState>

    var currentEpisode: PlayerEpisode?

    var playerSpeed: Duration

    fun addToQueue(episode: PlayerEpisode)

    fun removeAllFromQueue()

    fun play()

    fun play(playerEpisode: PlayerEpisode)

    fun play(playerEpisodes: List<PlayerEpisode>)

    fun pause()

    fun stop()

    fun next()

    fun previous()

    fun advanceBy(duration: Duration)

    fun rewindBy(duration: Duration)

    fun onSeekingStarted()

    fun onSeekingFinished(duration: Duration)

    fun increaseSpeed(speed: Duration = Duration.ofMillis(500))

    fun decreaseSpeed(speed: Duration = Duration.ofMillis(500))

    /**
     * ✅ Returns the current playback position in milliseconds.
     * Used to persist progress in the database.
     */
    fun currentPositionMs(): Long
}
