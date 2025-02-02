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
import org.ileewe.theseedjcli.databinding.FragmentSermonsBinding
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.ui.activities.MainActivity
import org.ileewe.theseedjcli.utils.Resource
import org.ileewe.theseedjcli.viewmodel.Factory.SermonViewModelProviderFactory
import org.ileewe.theseedjcli.viewmodel.SermonsViewModel

class SermonsFragment : Fragment() {

    private var _binding: FragmentSermonsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sermonsViewModel: SermonsViewModel by viewModels {
        SermonViewModelProviderFactory((requireActivity().application as SeedApplication).sermonRepository)
    }


    private lateinit var sermonAdapter: BigPostAdapter

    companion object {
        val TAG: String = SermonsFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSermonsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Initialize the recyclerView
        sermonSetup()

        //Article clicks
        sermonAdapter.setOnItemClickListener {
            navigateToDetails(it)
        }

        //Setup observers
        sermonsViewModel.getSermons().observe(viewLifecycleOwner) {
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
                        noSermonsFound.visibility = View.VISIBLE
                        rvTheSermons.visibility = View.GONE
                    }

                }else {
                    binding.apply {
                        noSermonsFound.visibility = View.GONE
                        rvTheSermons.visibility = View.VISIBLE
                        it.data?.let {
                            sermonAdapter.post = it
                        }
                    }
                }

            }else {
                Log.d(HomeFragment.TAG, "onChanged: Nothing to display")
            }
        }

        return root
    }

    private fun sermonSetup() {

        //Get the grid column counts to be used as SPAN for the GridLayoutManager
        val gridColumnSpan = resources.getInteger(R.integer.grid_column_count)
        binding.rvTheSermons.apply {
            sermonAdapter = BigPostAdapter()
            adapter = sermonAdapter
            layoutManager = GridLayoutManager(requireActivity(), gridColumnSpan) //LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun navigateToDetails(post: Post): Unit {
        findNavController().navigate(
            SermonsFragmentDirections.actionNavigationSermonsToDetailsFragment(post)
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
        Analytics.recordSectionVisit(requireActivity(), "Sermons")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}