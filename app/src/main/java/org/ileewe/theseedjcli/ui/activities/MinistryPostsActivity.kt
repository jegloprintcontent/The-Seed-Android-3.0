package org.ileewe.theseedjcli.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.adapter.BigPostAdapter
import org.ileewe.theseedjcli.application.SeedApplication
import org.ileewe.theseedjcli.databinding.MinistryPostsBinding
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.ui.activities.MinistryPostDetailActivity.Companion.MINISTRY_POST
import org.ileewe.theseedjcli.ui.fragments.HomeFragment
import org.ileewe.theseedjcli.utils.Resource
import org.ileewe.theseedjcli.viewmodel.Factory.MinistriesViewModelProviderFactory
import org.ileewe.theseedjcli.viewmodel.MinistriesViewModel

class MinistryPostsActivity : AppCompatActivity() {

    private lateinit var binding: MinistryPostsBinding
    private val ministriesViewModel: MinistriesViewModel by viewModels {
        MinistriesViewModelProviderFactory((application as SeedApplication).ministriesRepository)
    }
    private lateinit var ministriesAdapter: BigPostAdapter


    companion object {
        private val TAG =  MinistryPostsActivity::class.simpleName
        const val CATEGORY_NAME = "categoryName"
        const val CATEGORY_ID = "categoryId"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MinistryPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //access the intent extras
        val categoryId = intent.getIntExtra(CATEGORY_ID, 0)
        val categoryName = intent.getStringExtra(CATEGORY_NAME)


        Log.d(TAG, "onNavigate: navigate to read posts for $categoryName category with id: $categoryId")


        supportActionBar?.title =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(categoryName, Html.FROM_HTML_MODE_LEGACY)
            }else{
                Html.fromHtml(categoryName)
            }

        ministriesSetup()

        //article clicks
        ministriesAdapter.setOnItemClickListener {
            navigateToDetails(it)
        }

        ministriesViewModel.currentCategoryId.value = categoryId
        ministriesViewModel.currentCategoryId.observe(this) {
            Log.d(TAG, "The selected category is $it")
            ministriesViewModel.getLatestMinistries(20, it)
        }

        ministriesViewModel.getMinistries().observe(this) {
            if(it != null) {

                when(it.status) {

                    Resource.Status.LOADING-> {
                        Log.d(HomeFragment.TAG, "onChanged: LOADING with status ${it.status}")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.d(HomeFragment.TAG, "onChanged: SUCCESS with status ${it.status}")
                    }
                    else -> {
                        Log.d(HomeFragment.TAG, "onChanged: ERROR with status ${it.status}")
                    }
                }

                if(it.data?.size == 0) {
                    Log.d(HomeFragment.TAG, "onChanged: ERROR - No DATA TO DISPLAY")
                    binding.apply {
                        noMinistriesPostFound.visibility = View.VISIBLE
                        rvMinistryPosts.visibility = View.GONE
                    }

                }else {
                    binding.apply {
                        noMinistriesPostFound.visibility = View.GONE
                        rvMinistryPosts.visibility = View.VISIBLE
                        it.data?.let {
                            ministriesAdapter.post = it
                        }
                    }
                }

            }else {
                Log.d(HomeFragment.TAG, "onChanged: Nothing to display")
            }
        }

    }

    private fun ministriesSetup() {

        val gridColumnSpan = resources.getInteger(R.integer.grid_column_count)
        binding.rvMinistryPosts.apply {
            ministriesAdapter = BigPostAdapter()
            adapter = ministriesAdapter
            layoutManager = GridLayoutManager(this@MinistryPostsActivity, gridColumnSpan) //LinearLayoutManager(this@MinistryPostsActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun navigateToDetails(it: Post) {

        val intent = Intent(this, MinistryPostDetailActivity::class.java).apply {
            putExtra(MINISTRY_POST, it)
        }
        startActivity(intent)
    }

}