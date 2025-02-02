package org.ileewe.theseedjcli.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import org.ileewe.theseedjcli.dao.PostDao
import org.ileewe.theseedjcli.executor.AppExecutors
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.requests.PostService
import org.ileewe.theseedjcli.requests.response.ApiResponse
import org.ileewe.theseedjcli.utils.Constants.DEVOTIONS_CATEGORY
import org.ileewe.theseedjcli.utils.NetworkBoundResource
import org.ileewe.theseedjcli.utils.Resource

class SeedRepository(private val postDao: PostDao, private val executors: AppExecutors) {


    fun getSeeds(per_page: Int, category: Int = DEVOTIONS_CATEGORY): LiveData<Resource<List<Post>>> {
        return object: NetworkBoundResource<List<Post>, List<Post>>(executors) {
            override fun saveCallResult(item: List<Post>) {
                postDao.insertPosts(item)
            }

            override fun shouldFetch(data: List<Post>?): Boolean {
                return false
            }

            override fun loadFromDb(): LiveData<List<Post>> {
                return postDao.getLatestSeeds(category)
            }

            override fun createCall(): LiveData<ApiResponse<List<Post>>> {
                //return PostService.post.getPosts(per_page)
                return PostService.post.getPosts()
            }

        }.asLiveData()


    }
}