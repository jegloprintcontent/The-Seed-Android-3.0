package org.ileewe.theseedjcli.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.ileewe.theseedjcli.R
import org.ileewe.theseedjcli.adapter.BigMinistryAdapter
import org.ileewe.theseedjcli.analytics.Analytics
import org.ileewe.theseedjcli.application.SeedApplication
import org.ileewe.theseedjcli.databinding.FragmentMinistriesBinding
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.ui.activities.MainActivity
import org.ileewe.theseedjcli.ui.activities.MinistryPostDetailActivity
import org.ileewe.theseedjcli.ui.activities.MinistryPostsActivity
import org.ileewe.theseedjcli.viewmodel.Factory.MinistriesViewModelProviderFactory
import org.ileewe.theseedjcli.viewmodel.MinistriesViewModel


class MinistriesFragment : Fragment() {

    companion object {
        private val TAG = MinistriesFragment::class.java.simpleName
    }

    private var _binding: FragmentMinistriesBinding? = null

    private val binding get() = _binding!!
    private val ministriesViewModel: MinistriesViewModel by viewModels {
        MinistriesViewModelProviderFactory((requireActivity().application as SeedApplication).ministriesRepository)
    }

    private lateinit var ministriesAdapter: BigMinistryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMinistriesBinding.inflate(layoutInflater, container, false)
        val root: View = binding.root

        //Initialize the recyclerView
        ministriesSetup()

        //article clicks
        ministriesAdapter.setOnItemClickListener {
            navigateToDetails(it)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ministriesViewModel.allMinistries.observe(viewLifecycleOwner) {
            ministries ->
            ministries.let {
                if(it.isNotEmpty()) {
                    ministriesAdapter.categories = it
                    binding.noMinistriesFound.visibility = View.GONE
                    binding.rvTheMinistries.visibility = View.VISIBLE

                }else {
                    binding.rvTheMinistries.visibility = View.GONE
                    binding.noMinistriesFound.visibility = View.VISIBLE
                }

            }

        }


    }

    private fun navigateToDetails(it: Category) {
        Log.d(HomeFragment.TAG, "OnClick: The category clicked is - $it ")
        val intent = Intent(requireContext(), MinistryPostsActivity::class.java).apply {
            putExtra(MinistryPostsActivity.CATEGORY_ID, it.id)
            putExtra(MinistryPostsActivity.CATEGORY_NAME, it.name)
        }

        startActivity(intent)
    }

    private fun ministriesSetup() {

        val gridColumnSpan = resources.getInteger(R.integer.grid_column_count)
        binding.rvTheMinistries.apply {
            ministriesAdapter = BigMinistryAdapter()
            adapter = ministriesAdapter
            layoutManager = GridLayoutManager(requireContext(), gridColumnSpan) //LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
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
        Analytics.recordSectionVisit(requireActivity(), "Ministries")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}