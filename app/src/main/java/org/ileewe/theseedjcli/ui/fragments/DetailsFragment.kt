package org.ileewe.theseedjcli.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.analytics.Analytics
import org.ileewe.theseedjcli.databinding.FragmentDetailsBinding
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.ui.activities.MainActivity
import org.ileewe.theseedjcli.ui.viewmodels.PostViewModel
import org.ileewe.theseedjcli.utils.AudioPlayer
import org.ileewe.theseedjcli.utils.DateTimeUtils
import org.ileewe.theseedjcli.utils.WebHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DetailsFragment : Fragment() {
    private var binding: FragmentDetailsBinding? = null
    private lateinit var doc: Document
    private var html: String? = null
    private var article: Post? = null
    private var isPlaying = false

    private lateinit var audioPlayer: AudioPlayer
    private lateinit var fabPlay: FloatingActionButton
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailsFragmentArgs by navArgs()

        // Stores the selected post in ViewModel
        postViewModel.setPost(args.articleDetail)

        // Retrieves the post from ViewModel
        article = postViewModel.selectedPost.value

        // Parsing article content into better HTML format
        doc = Jsoup.parse(article?.content?.rendered ?: "")
        html = WebHelper.docToBetterHTML(requireActivity(), doc)

        // Sets action bar title
        (activity as MainActivity).supportActionBar?.title =
            Html.fromHtml(article?.title?.rendered ?: "", Html.FROM_HTML_MODE_LEGACY)

        // Initializes AudioPlayer
        audioPlayer = AudioPlayer()
        fabPlay = view.findViewById(R.id.fab_audio_play)

        binding?.apply {
            // Loads Image with Glide
            article?.image?.let {
                Glide.with(requireActivity())
                    .load(it)
                    .placeholder(R.drawable.image_background)
                    .error(R.drawable.image_background)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(articleImage)
            }

            // Sets title and article time
            articleTitle.text = Html.fromHtml(article?.title?.rendered ?: "", Html.FROM_HTML_MODE_LEGACY)
            articleTime.text = DateTimeUtils.getDate(article?.date ?: "")
        }

        // Fix WebView Content Loading
        setupWebView()

        //  Floating Button Controls
        setupAudioPlayer()
    }

    private fun setupWebView() {
        binding!!.contentView.apply {
            setBackgroundColor(Color.TRANSPARENT)
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true
            settings.defaultFontSize = 16

            // Handles video loading
            webChromeClient = object : WebChromeClient() {
                override fun getDefaultVideoPoster(): Bitmap? {
                    return BitmapFactory.decodeResource(resources, R.drawable.image_background)
                }
            }

            // Day/Night mode support
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                val nightModeFlag: Int = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
                    WebSettingsCompat.setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON)
                }
            }

            html?.let {
                loadDataWithBaseURL(null, it, "text/html", "UTF-8", null)
            }
        }
    }

    private fun setupAudioPlayer() {
        fabPlay.setImageResource(R.drawable.ic_play_arrow)

        fabPlay.setOnClickListener {
            val audioUrl = article?.audioURL // Corrected field name to `audioURL`

            // Log the audio URL for debugging purposes
            Log.d("Audio URL", "Audio URL: $audioUrl")

            // Check if audio URL is available
            if (audioUrl.isNullOrBlank()) {
                Toast.makeText(requireContext(), "No audio available for this post", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Handle audio playback (play or pause)
            if (isPlaying) {
                audioPlayer.stopAudio()
                fabPlay.setImageResource(R.drawable.ic_play_arrow)  // Play icon
            } else {
                audioPlayer.playAudio(audioUrl)
                fabPlay.setImageResource(R.drawable.ic_pause)  // Pause icon
            }
            isPlaying = !isPlaying
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.stopAudio()
        binding = null
    }
}
