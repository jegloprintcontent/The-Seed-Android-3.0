package org.ileewe.theseedjcli.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.adapter.BigPostAdapter
import org.ileewe.theseedjcli.analytics.Analytics
import org.ileewe.theseedjcli.application.SeedApplication
import org.ileewe.theseedjcli.databinding.FragmentTheSeedBinding
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.ui.activities.MainActivity
import org.ileewe.theseedjcli.utils.Resource
import org.ileewe.theseedjcli.viewmodel.Factory.SeedViewModelProviderFactory
import org.ileewe.theseedjcli.viewmodel.SeedViewModel

class TheSeedFragment : Fragment() {

    private var _binding: FragmentTheSeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val seedViewModel: SeedViewModel by viewModels {
        SeedViewModelProviderFactory((requireActivity().application as SeedApplication).seedRepository)
    }

    private lateinit var seedAdapter: BigPostAdapter

    companion object {
        val TAG: String = TheSeedFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTheSeedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Initialize the recyclerView
        seedSetup()

        //article clicks
        seedAdapter.setOnItemClickListener {
            navigateToDetails(it)
        }

        //Setup observers
        seedViewModel.getSeeds().observe(viewLifecycleOwner) {
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
                        //homeLatestSermon.visibility = View.GONE
                        /*sectionDevotion.visibility = View.GONE
                        sectionSermons.visibility = View.GONE*/
                    }

                }else {
                    binding.apply {
                        it.data.let {
                            if (it != null) {
                                seedAdapter.post = it
                            }
                        }

                    }
                }

            }else {
                Log.d(HomeFragment.TAG, "onChanged: Nothing to display")
            }
        }

        return root
    }


    private fun seedSetup() {

        val gridColumnSpan = resources.getInteger(R.integer.grid_column_count)
        binding.rvTheSeeds.apply {
            seedAdapter = BigPostAdapter()
            adapter = seedAdapter
            layoutManager = GridLayoutManager(requireActivity(), gridColumnSpan) //LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun navigateToDetails(post: Post): Unit {
        findNavController().navigate(
            TheSeedFragmentDirections.actionNavigationTheSeedToDetailsFragment(post)
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?).let {
                it?.hideBottomNavigationView()
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
        Analytics.recordSectionVisit(requireActivity(), "Devotions - The Seed")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}