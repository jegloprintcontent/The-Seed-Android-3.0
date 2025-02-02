package org.ileewe.theseedjcli.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.requests.PostAPI
import org.ileewe.theseedjcli.requests.RetrofitService
import org.ileewe.theseedjcli.requests.response.ApiResponse

class PostViewModel : ViewModel() {
    private val _selectedPost = MutableLiveData<Post?>()
    val selectedPost: LiveData<Post?> get() = _selectedPost

    fun setPost(post: Post) {
        _selectedPost.value = post
    }
    private val postAPI: PostAPI = RetrofitService.createService(PostAPI::class.java)

    private val _postsLiveData = MutableLiveData<ApiResponse<List<Post>>>()
    val postsLiveData: LiveData<ApiResponse<List<Post>>> get() = _postsLiveData

    fun getPosts() {
        _postsLiveData.postValue(ApiResponse.Loading) // ✅ Show loading state

        viewModelScope.launch {
            try {
                val responseLiveData: LiveData<ApiResponse<List<Post>>> = postAPI.getPosts()
                responseLiveData.observeForever { response ->
                    _postsLiveData.postValue(response) // ✅ Post the observed response
                }
            } catch (e: Exception) {
                _postsLiveData.postValue(ApiResponse.Error(e)) // ✅ Proper error handling
            }
        }
    }
}
