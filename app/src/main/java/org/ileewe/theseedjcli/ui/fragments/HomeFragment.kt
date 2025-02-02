package org.ileewe.theseedjcli.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.FirebaseApp
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.adapter.CategoryAdapter
import org.ileewe.theseedjcli.adapter.PostAdapter
import org.ileewe.theseedjcli.analytics.Analytics
import org.ileewe.theseedjcli.application.SeedApplication
import org.ileewe.theseedjcli.databinding.FragmentHomeBinding
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.remoteconfig.RemoteConfigUtils
import org.ileewe.theseedjcli.ui.activities.MainActivity
import org.ileewe.theseedjcli.ui.activities.MinistryPostsActivity
import org.ileewe.theseedjcli.ui.activities.MinistryPostsActivity.Companion.CATEGORY_ID
import org.ileewe.theseedjcli.ui.activities.MinistryPostsActivity.Companion.CATEGORY_NAME
import org.ileewe.theseedjcli.utils.DateTimeUtils
import org.ileewe.theseedjcli.utils.Resource
import org.ileewe.theseedjcli.viewmodel.Factory.HomeViewModelProviderFactory
import org.ileewe.theseedjcli.viewmodel.HomeViewModel
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
// Audio implementation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Observe posts LiveData
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            // Update RecyclerView or UI with posts
        }

        // Fetch posts when the view is created
        viewModel.fetchPosts()

        // Handle play button click
//        binding.playButton.setOnClickListener {
//            val audioUrl = "https://your-audio-url.com/audio.mp3" // Replace with dynamic URL
//            viewModel.playAudio(audioUrl)
//        }

        // Handle stop button click
//        binding.stopButton.setOnClickListener {
//            viewModel.stopAudio()
//        }
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelProviderFactory((requireActivity().application as SeedApplication).repository)
    }

    private var devotionOffsets = mutableListOf<Post>()
    private lateinit var devotionAdapter: PostAdapter
    private lateinit var sermonsAdapter: PostAdapter
    private lateinit var ministriesAdapter: CategoryAdapter
    private lateinit var firebase: FirebaseApp

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebase = FirebaseApp.getInstance()

        //Setup remote config
        RemoteConfigUtils.getRemoteConfig(firebase)
            .setConfigSettingsAsync(RemoteConfigUtils.configSettings)
        RemoteConfigUtils.getRemoteConfig(firebase).fetchAndActivate().addOnCompleteListener {
            if(it.isSuccessful) {
                updateView()
                Log.d(TAG, "RemoteConfig: Value successfully retrieved")

            }else {
                Log.d(TAG, "RemoteConfig: Unable to retrieve value from remote server")
            }
        }

        //Initialize recyclers
        devotionSetup()
        sermonSetup()
        ministriesSetup()

        //Adapter item click
        /*sermonAdapter.setOnItemClickListener {
            val bundle: Bundle = Bundle().apply {
                putParcelable("post", it)
            }
        }*/

        devotionAdapter.setOnItemClickListener {
            this.navigateToDetails(it)
        }

        sermonsAdapter.setOnItemClickListener {
            this.navigateToDetails(it)
        }

        ministriesAdapter.setOnItemClickListener {
            Log.d(TAG, "OnClick: The category clicked is - $it ")

           val intent = Intent(requireContext(), MinistryPostsActivity::class.java).apply {
               putExtra(CATEGORY_ID, it.id)
               putExtra(CATEGORY_NAME, it.name)
           }

            startActivity(intent)

        }

        //`setup Observers
        homeViewModel.getAllPost().observe(viewLifecycleOwner) {
            if(it != null) {
                when(it.status) {
                    Resource.Status.LOADING-> {
                        Log.d(TAG, "onChanged: LOADING with status ${it.status}")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.d(TAG, "onChanged: SUCCESS with status ${it.status}")
                    }
                    else -> {
                        Log.d(TAG, "onChanged: ERROR with status ${it.status}")
                    }
                }
                if(it.data?.size == 0) {
                    Log.d(TAG, "onChanged: ERROR - No DATA TO DISPLAY")
                    binding.apply {
                        /*homeLatestSermon.visibility = View.GONE
                        sectionDevotion.visibility = View.GONE
                        sectionSermons.visibility = View.GONE*/
                    }

                }else {

                    it.data?.forEachIndexed {index, post ->
                        if(index == 0) {
                            Log.d(TAG, "onChanged: $index  ${post.title.rendered} - ${post.categories.first()}")
                            populateLeadDevotion(post)
                            devotionOffsets.clear()

                        }else {
                            Log.d(TAG, "onChanged: $index  ${post.title.rendered} ${post.image.toString()}")
                            devotionOffsets.add(post)
                            //devotionOffsets += post
                        }
                    }

                    /*binding.devotions.visibility = View.VISIBLE
                    binding.homeLatestSermon.visibility = View.VISIBLE
                    binding.sectionDevotion.visibility = View.VISIBLE
                    binding.sectionSermons.visibility = View.VISIBLE*/
                    /*binding.apply {
                        homeLatestSermon.visibility = View.VISIBLE
                        sectionDevotion.visibility = View.VISIBLE
                        sectionSermons.visibility = View.VISIBLE
                    }*/
                    binding.devotions.visibility = View.VISIBLE
                    devotionAdapter.post = devotionOffsets
                }

            }else {
              Log.d(TAG, "onChanged: Nothing to display")
            }
        }


        //observer for sermon changes
        homeViewModel.allSermons().observe(viewLifecycleOwner) {
            if(it != null) {
                when(it.status) {
                    Resource.Status.LOADING -> {
                        Log.d(HomeFragment.TAG, "onChanged: SERMONS - LOADING with status ${it.status}")
                    }

                    Resource.Status.SUCCESS -> {
                        Log.d(HomeFragment.TAG, "onChanged: SERMONS - SUCCESS with status ${it.status}")
                    }
                    else -> {
                        Log.d(HomeFragment.TAG, "onChanged: SERMONS - ERROR with status ${it.status}")
                    }
                }

                if(it.data?.size == 0) {
                    Log.d(HomeFragment.TAG, "onChanged: SERMONS - ERROR - No DATA TO DISPLAY")
                    binding.apply {
                        sectionSermons.visibility = View.GONE
                        sermons.visibility = View.GONE
                    }

                }else {
                    binding.apply {
                        sectionSermons.visibility = View.VISIBLE
                        sermons.visibility = View.VISIBLE
                        it.data?.let {
                            sermonsAdapter.post = it
                        }
                    }
                }

            }else {
                Log.d(HomeFragment.TAG, "onChanged: Nothing to display")
            }


        }

//        homeViewModel.allSermons.observe(viewLifecycleOwner) {
//                sermons ->
//            sermons.let {
//                if(it.isNotEmpty()) {
//                    sermonsAdapter.post = it
//                    binding.sectionSermons.visibility = View.VISIBLE
//                }else {
//                    binding.sectionSermons.visibility = View.GONE
//                }
//
//            }
//        }

        //Observer for ministries
        homeViewModel.allMinistries().observe(viewLifecycleOwner) {

            if(it != null) {

                when(it.status) {

                    Resource.Status.LOADING-> {
                        Log.d(HomeFragment.TAG, "onChanged: MINISTRIES - LOADING with status ${it.status}")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.d(HomeFragment.TAG, "onChanged: MINISTRIES - SUCCESS with status ${it.status}")
                    }
                    else -> {
                        Log.d(HomeFragment.TAG, "onChanged: MINISTRIES - ERROR with status ${it.status}")
                    }
                }

                if(it.data?.size == 0) {
                    Log.d(HomeFragment.TAG, "onChanged: MINISTRIES - ERROR - No DATA TO DISPLAY")
                    binding.apply {
                        sectionMinistries.visibility = View.GONE
                        ministries.visibility = View.GONE
                    }

                }else {
                    binding.apply {
                        sectionMinistries.visibility = View.VISIBLE
                        ministries.visibility = View.VISIBLE
                        it.data?.let {
                            ministriesAdapter.categories = it
                        }
                    }
                }

            }else {
                Log.d(HomeFragment.TAG, "onChanged: Nothing to display")
            }

        }


        //OnClick Events
        binding.sectionDevotionMore.setOnClickListener(this)
        binding.sectionSermonMore.setOnClickListener(this)
        binding.sectionMinistriesMore.setOnClickListener(this)
        binding.remoteBtnTitle.setOnClickListener(this)
        /*binding.sectionDevotionMore.setOnClickListener{
                findNavController().navigate(R.id.navigation_the_seed)
        }*/

        return root
    }

    private fun populateLeadDevotion(post: Post) {
        binding.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                leadSermonTitle.text = Html.fromHtml(post.title.rendered, Html.FROM_HTML_MODE_LEGACY)
            } else {
                leadSermonTitle.text = Html.fromHtml(post.title.rendered)
            }

            leadSermonCategory.text = getString(R.string.lead_article_category_title)
            // leadTime.text = DateTimeUtil.getTimeAgo(article.date)
            leadSermonDate.text = DateTimeUtils.getDate(post.date)

            post.image?.let {
                Glide
                    .with(this@HomeFragment)
                    .load(post.image)
                    .placeholder(R.drawable.image_background)
                    .error(R.drawable.image_background)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(leadSermonImage)
            }

            leadSermonImage.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.overlay), android.graphics.PorterDuff.Mode.OVERLAY);

            homeLatestSermon.setOnClickListener{
                navigateToDetails(post)
            }
        }

    }


    private fun devotionSetup() {
        binding.devotions.apply {
            devotionAdapter = PostAdapter()
            adapter = devotionAdapter
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun sermonSetup() {
        binding.sermons.apply {
            sermonsAdapter = PostAdapter()
            adapter = sermonsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun ministriesSetup() {
        binding.ministries.apply {
            ministriesAdapter = CategoryAdapter()
            adapter = ministriesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun navigateToDetails(post: Post): Unit {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(post)
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?).let {
                it?.hideBottomNavigationView()
            }
        }
    }


    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.section_devotion_more -> {
                    findNavController().navigate(R.id.navigation_the_seed)
                    return
                }

                R.id.section_sermon_more -> {
                    findNavController().navigate(R.id.navigation_sermons)
                }

                R.id.section_ministries_more -> {
                    findNavController().navigate(R.id.navigation_ministries)
                }

                R.id.remote_btn_title -> {
                   //Start an activity that can interpret this event
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(RemoteConfigUtils.getRemoteConfig(firebase)
                            .getString(RemoteConfigUtils.KEY_ACTION_LINK_URL))))
                    }catch (e: ActivityNotFoundException) {
                        Toast.makeText(requireContext(),
                            getString(R.string.no_application_found), Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }


    private fun updateView() {

        if(RemoteConfigUtils.getRemoteConfig(firebase).getBoolean(RemoteConfigUtils.KEY_DISPLAY_VIEW)) {
            if(_binding != null) {
                binding.remoteEvents.visibility = View.VISIBLE
                binding.remoteTextTitle.text = RemoteConfigUtils.getRemoteConfig(firebase)
                    .getString(RemoteConfigUtils.KEY_ACTION_LINK_TEXT)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity?).let {
                it?.showBottomNavigationView()
            }
        }

        //Analytics
        Analytics.recordSectionVisit(requireActivity(), TAG)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}