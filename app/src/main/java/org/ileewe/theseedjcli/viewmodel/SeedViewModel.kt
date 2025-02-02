package org.ileewe.theseedjcli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.repository.SeedRepository
import org.ileewe.theseedjcli.utils.Resource

class SeedViewModel(private val seedRepository: SeedRepository) : ViewModel() {

    private val latestSeeds: MediatorLiveData<Resource<List<Post>>> = MediatorLiveData()

    init {
        getLatestSeeds(20)
    }

    fun getSeeds(): LiveData<Resource<List<Post>>> {
        return latestSeeds
    }


    //Using the NetworkBoundResource class
    private fun getLatestSeeds (per_page: Int) {

        val repository: LiveData<Resource<List<Post>>> = seedRepository.getSeeds(per_page)
        latestSeeds.addSource(repository) {
                postList ->
            latestSeeds.value = postList
        }

    }
}