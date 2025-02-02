//import android.os.Bundle
//import android.view.View
//import android.widget.ImageButton
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import org.ileewe.theseedjcli.viewmodels.AudioPlayerViewModel
//
//class DetailsActivity : AppCompatActivity() {
//    private val audioViewModel: AudioPlayerViewModel by viewModels { AudioPlayerViewModelFactory(this) }
//    private lateinit var playPauseButton: ImageButton
//    private var audioUrl: String? = null  // Passed from HomeView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_details)
//
//        playPauseButton = findViewById(R.id.floating_audio_button)
//
//        // Get the audio URL from the Intent
//        audioUrl = intent.getStringExtra("AUDIO_URL")
//
//        // Show button only if there's an audio URL
//        if (audioUrl.isNullOrEmpty()) {
//            playPauseButton.visibility = View.GONE
//        } else {
//            playPauseButton.visibility = View.VISIBLE
//        }
//
//        playPauseButton.setOnClickListener {
//            audioUrl?.let {
//                audioViewModel.togglePlayPause()
//                updateButtonUI()
//            }
//        }
//    }
//
//    private fun updateButtonUI() {
//        if (audioViewModel.isPlaying) {
//            playPauseButton.setImageResource(R.drawable.ic_pause)
//        } else {
//            playPauseButton.setImageResource(R.drawable.ic_play)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        audioViewModel.stopAudio()
//    }
//}
