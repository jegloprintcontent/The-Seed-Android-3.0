//package org.ileewe.theseedjcli.viewmodels
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import com.google.android.exoplayer2.ExoPlayer
//import com.google.android.exoplayer2.MediaItem
//
//class AudioPlayerViewModel(context: Context) : ViewModel() {
//    private var exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
//    var isPlaying: Boolean = false
//        private set
//
//    fun playAudio(url: String) {
//        val mediaItem = MediaItem.fromUri(url)
//        exoPlayer.setMediaItem(mediaItem)
//        exoPlayer.prepare()
//        exoPlayer.play()
//        isPlaying = true
//    }
//
//    fun togglePlayPause() {
//        if (exoPlayer.isPlaying) {
//            exoPlayer.pause()
//            isPlaying = false
//        } else {
//            exoPlayer.play()
//            isPlaying = true
//        }
//    }
//
//    fun stopAudio() {
//        exoPlayer.stop()
//        exoPlayer.clearMediaItems()
//        isPlaying = false
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        exoPlayer.release()
//    }
//}
