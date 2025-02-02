package org.ileewe.theseedjcli.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import org.ileewe.theseedjcli.executor.AppExecutors
import org.ileewe.theseedjcli.requests.response.ApiResponse
import org.ileewe.theseedjcli.requests.response.ApiResponse.*

abstract class NetworkBoundResource<CacheObject, RequestObject>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<CacheObject>>()

    init {

        // Updating liveData for loading status
        result.value = Resource.loading(null)

        // Observing liveData source from local db
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()

        // Add the database source to the MediatorLiveData
        result.addSource(dbSource) { data ->
            // After triggering the onChange method here by calling the data in the database,
            // remove it and try to load data from the network.
            result.removeSource(dbSource)

            // Fetching the data from the network and update cache
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                // Otherwise, reusing the initial dbSource
                result.addSource(dbSource) { newData ->
                    Resource.success(newData)?.let { setValue(it) }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<CacheObject>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    /**
     * 1) Observe the local db
     * 2) If the condition/query network
     * 3) Stop observing the local db
     * 4) Insert new data into the local db
     * 5) Begin observing the local db again to see refreshed data from the network
     * @param dbSource
     */
    private fun fetchFromNetwork(dbSource: LiveData<CacheObject>) {

        Log.d("TAG", "fetchFromNetwork: called")

        // Re-attaching dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            Resource.loading(newData)?.let {
                setValue(it)
            }
        }

        // API call to get the ApiResponse
        val apiResponse = createCall()

        // Adding it as a source to the MediatorLiveData
        result.addSource(apiResponse) { response ->
            // Removing the sources added so far so we can handle the different cases from
            // the apiResponse result
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            when (response) {
                is Success -> {
                    Log.d("TAG", "onChanged: Success")
                    appExecutors.diskIO.execute {
                        // Saving the response to the database on a background thread
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread.execute {
                            // Requesting a new live data, otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            result.addSource(loadFromDb()) { newData ->
                                Resource.success(newData)?.let {
                                    setValue(it)
                                }
                            }
                        }
                    }
                }
                is Loading -> {
                    Log.d("TAG", "onChanged: Loading")
                    // You can handle the loading state if you want
                }
                is Error -> {
                    Log.d("TAG", "onChanged: Error")
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        Resource.error(response.exception.message ?: "Unknown error", newData)?.let {
                            setValue(it)
                        }
                    }
                }
            }
        }
    }

    @WorkerThread
    protected open fun processResponse(response: Success<RequestObject>) = response.data

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestObject)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: CacheObject?): Boolean

    // Called to get the cached data from the database.
    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheObject>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestObject>>

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData() = result as LiveData<Resource<CacheObject>>
}
