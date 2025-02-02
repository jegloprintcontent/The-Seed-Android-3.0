package org.ileewe.theseedjcli.utils

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

class AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null

    // Plays audio from the given URL
    fun playAudio(audioUrl: String) {
        if (audioUrl.isEmpty()) {
            Log.e("AudioPlayer", "Error: Audio URL is empty")
            return
        }

        try {
            mediaPlayer?.release() // Releases any previous media player
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                setOnPreparedListener { start() }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "Error: MediaPlayer error $what, $extra")
                    false
                }
                prepareAsync()
            }
        } catch (e: IOException) {
            Log.e("AudioPlayer", "IOException: ${e.message}")
        }
    }


    // Stops the audio and release the player
    fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
