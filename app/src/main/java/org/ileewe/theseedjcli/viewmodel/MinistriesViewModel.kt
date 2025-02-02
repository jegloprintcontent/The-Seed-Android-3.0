package org.ileewe.theseedjcli.viewmodel

import android.util.Log
import android.util.MutableInt
import androidx.lifecycle.*
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.repository.MinistriesRepository
import org.ileewe.theseedjcli.utils.Constants
import org.ileewe.theseedjcli.utils.Resource

class MinistriesViewModel(private val ministriesRepository: MinistriesRepository) : ViewModel() {

    private val latestMinistries: MediatorLiveData<Resource<List<Post>>> = MediatorLiveData()
    val currentCategoryId: MutableLiveData<Int> = MutableLiveData()

    init {

    }

    fun getMinistries(): LiveData<Resource<List<Post>>> = latestMinistries

    fun getLatestMinistries(per_page: Int, categoryId: Int) {
        val repository: LiveData<Resource<List<Post>>> = ministriesRepository.getMinistries(per_page, categoryId)
        latestMinistries.addSource(repository) {
            posts ->
            latestMinistries.value = posts
        }
    }

    val allMinistries: LiveData<List<Category>> = ministriesRepository.ministryCategories(Constants.MINISTRIES_CATEGORY).asLiveData()

}