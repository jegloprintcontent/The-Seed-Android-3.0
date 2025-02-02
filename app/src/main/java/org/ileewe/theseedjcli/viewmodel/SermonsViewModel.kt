package org.ileewe.theseedjcli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.repository.SermonsRepository
import org.ileewe.theseedjcli.utils.Resource

class SermonsViewModel(private val sermonRepository: SermonsRepository) : ViewModel() {

    private val latestSermons: MediatorLiveData<Resource<List<Post>>> = MediatorLiveData()

    init {
        getLatestSermons(20)
    }

    fun getSermons(): LiveData<Resource<List<Post>>> {
        return latestSermons
    }


    //Using the NetworkBoundResource class
    private fun getLatestSermons (per_page: Int) {

        val repository: LiveData<Resource<List<Post>>> = sermonRepository.getSermons(per_page)
        latestSermons.addSource(repository) {
                postList ->
            latestSermons.value = postList
        }

    }

}