package org.ileewe.theseedjcli.ui.activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.analytics.Analytics
import org.ileewe.theseedjcli.databinding.ActivityMinistryPostDetailBinding
import org.ileewe.theseedjcli.databinding.FragmentDetailsBinding
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.utils.DateTimeUtils
import org.ileewe.theseedjcli.utils.WebHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class MinistryPostDetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var doc: Document
    private var html: String? = null
    private var article: Post? = null
    var title: String? = null
    var link: String? = null
    var isShare: Boolean = true
    var post: Post? = null
    companion object {
        private val TAG = "Ministries"
        const val MINISTRY_POST = "ministry_post"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = this.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        post = intent.getParcelableExtra<Post>(MINISTRY_POST)
        post?.let {
            supportActionBar?.title =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml( post?.title?.rendered, Html.FROM_HTML_MODE_LEGACY)
                }else{
                    Html.fromHtml( post?.title?.rendered )
                }


            doc = Jsoup.parse(post?.content!!.rendered)
            html = WebHelper.docToBetterHTML(this,doc)


            title = Jsoup.parse(post?.title!!.rendered).text()
            link = post?.link



            binding.apply {

               post?.image?.let {
                    Glide.with(this@MinistryPostDetailActivity)
                        .load(post?.image)
                        .placeholder(R.drawable.image_background)
                        .error(R.drawable.image_background)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(articleImage)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    articleTitle.text = Html.fromHtml( post?.title?.rendered, Html.FROM_HTML_MODE_LEGACY)
                }else{
                    @Suppress("DEPRECATION")
                    articleTitle.text = Html.fromHtml( post?.title?.rendered)
                }

                articleTime.text = DateTimeUtils.getDate(post?.date!!)
            }

            binding!!.contentView.apply {

                setBackgroundColor(Color.TRANSPARENT)
                settings.javaScriptEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.domStorageEnabled = true
                settings.defaultFontSize = 16



                setWebChromeClient(object : WebChromeClient() {
                    override fun getDefaultVideoPoster(): Bitmap? {
                        return if (super.getDefaultVideoPoster() == null) {
                            BitmapFactory.decodeResource(resources, R.drawable.image_background)
                        } else {
                            super.getDefaultVideoPoster()
                        }
                    }
                })


                //Checking for support on dayNightMode within the webView context
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {

                    //Check the dayNightMode of the device
                    val nightModeFlag: Int =
                        this.resources.getConfiguration().uiMode and
                                Configuration.UI_MODE_NIGHT_MASK
                    when (nightModeFlag) {
                        Configuration.UI_MODE_NIGHT_NO -> {}
                        Configuration.UI_MODE_NIGHT_YES -> WebSettingsCompat.setForceDark(
                            getSettings(),
                            WebSettingsCompat.FORCE_DARK_ON
                        )
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
                    }
                    Log.d(
                        TAG,
                        "onCreate: DayNight mode value of device: $nightModeFlag"
                    )
                } else {
                    Log.d(
                        TAG,
                        "onCreate: DayNight mode value: Not supported"
                    )
                }


                html?.let {
                    loadDataWithBaseURL("https://theseedjcli.ileewe.org/", it,
                        "text/html",
                        "UTF-8",
                        null)
                }
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.action_share_article -> {
                val type = "text/plain"
                val subject = "Please read this article on the Seed App"
                var extraText = ""
                val shareWith = "Share with"

                post?.let {

                    extraText = "I came across this article on The Seed App, please have a look: \n\n  $title \n\n $link "

                }

                val intent = Intent()
                intent.apply {
                    setAction(Intent.ACTION_SEND)
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, extraText)
                    setType(type)
                }
                startActivity(
                    Intent.createChooser(
                    intent, shareWith
                ))

                //Share event
                Analytics.recordShareEvent(this@MinistryPostDetailActivity, title, link)

                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}