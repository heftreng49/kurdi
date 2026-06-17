package com.heftreng.kurdi.util

import android.media.MediaPlayer

object AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun oynat(url: String, onBitti: () -> Unit = {}) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(url)
                setOnPreparedListener { start() }
                setOnCompletionListener { onBitti() }
                setOnErrorListener { _, _, _ -> onBitti(); true }
                prepareAsync()
            } catch (e: Exception) {
                onBitti()
            }
        }
    }

    fun durdur() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
