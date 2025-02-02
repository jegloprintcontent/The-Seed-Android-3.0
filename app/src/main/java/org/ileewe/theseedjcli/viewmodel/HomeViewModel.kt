package org.ileewe.theseedjcli.viewmodel

import androidx.lifecycle.*
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.repository.HomeRepository
import org.ileewe.theseedjcli.utils.Constants.DEFAULT_NUMBER_OF_POST
import org.ileewe.theseedjcli.utils.Constants.MINISTRIES_CATEGORY
import org.ileewe.theseedjcli.utils.Resource
import kotlinx.coroutines.launch
import org.ileewe.theseedjcli.utils.AudioPlayer
import android.util.Log

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    // LiveData declarations
    private val latestPosts: MediatorLiveData<Resource<List<Post>>> = MediatorLiveData()
    private val allMinistries: MediatorLiveData<Resource<List<Category>>> = MediatorLiveData()
    private val allSermons: MediatorLiveData<Resource<List<Post>>> = MediatorLiveData()
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    // LiveData for tracking the currently playing audio
    private val _currentAudioUrl = MutableLiveData<String?>()
    val currentAudioUrl: LiveData<String?> get() = _currentAudioUrl

    // Audio Player
    private var audioPlayer: AudioPlayer? = null

    init {
        // Fetches data when ViewModel is initialized
        getLatestPosts(DEFAULT_NUMBER_OF_POST)
        getAllSermons()
        getAllMinistries(MINISTRIES_CATEGORY)

        // Initializes the audio player
        audioPlayer = AudioPlayer()
    }

    // Fetches and updates posts
    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val fetchedPosts = homeRepository.getPosts(DEFAULT_NUMBER_OF_POST).value?.data
                _posts.postValue(fetchedPosts ?: emptyList()) // Update LiveData
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching posts: ${e.message}")
            }
        }
    }

    fun getAllPost(): LiveData<Resource<List<Post>>> {
        return latestPosts
    }

    fun allMinistries(): LiveData<Resource<List<Category>>> {
        return allMinistries
    }

    fun allSermons(): LiveData<Resource<List<Post>>> {
        return allSermons
    }

    // Audio playback methods
    fun playAudio(audioUrl: String) {
        if (_currentAudioUrl.value == audioUrl) {
            Log.d("AudioPlayer", "Audio already playing: $audioUrl")
            return
        }

        stopAudio() // Stops any existing audio before playing new one
        _currentAudioUrl.postValue(audioUrl) // Update LiveData
        Log.d("AudioPlayer", "Playing new audio: $audioUrl")
        audioPlayer?.playAudio(audioUrl)
    }

    fun stopAudio() {
        Log.d("AudioPlayer", "Stopping audio playback")
        audioPlayer?.stopAudio()
        _currentAudioUrl.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio() // Ensure audio stops when ViewModel is destroyed
    }

    // Using NetworkBoundResource to fetch posts
    private fun getLatestPosts(per_page: Int) {
        val repository: LiveData<Resource<List<Post>>> = homeRepository.getPosts(per_page)
        latestPosts.addSource(repository) { postList ->
            latestPosts.value = postList
        }
    }

    private fun getAllMinistries(parentId: Int) {
        val repository: LiveData<Resource<List<Category>>> = homeRepository.getMinistries(parentId)
        allMinistries.addSource(repository) { ministries ->
            allMinistries.value = ministries
        }
    }

    private fun getAllSermons() {
        val repository: LiveData<Resource<List<Post>>> = homeRepository.getSermonPost()
        allSermons.addSource(repository) { sermons ->
            allSermons.value = sermons
        }
    }
}
