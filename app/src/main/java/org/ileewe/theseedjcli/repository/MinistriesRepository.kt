package org.ileewe.theseedjcli.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import org.ileewe.theseedjcli.dao.PostDao
import org.ileewe.theseedjcli.executor.AppExecutors
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.requests.PostService
import org.ileewe.theseedjcli.requests.response.ApiResponse
import org.ileewe.theseedjcli.utils.NetworkBoundResource
import org.ileewe.theseedjcli.utils.Resource

class MinistriesRepository(private val postDao: PostDao, private val executors: AppExecutors) {

    fun getMinistries(per_page: Int, category: Int): LiveData<Resource<List<Post>>> {
        return object : NetworkBoundResource<List<Post>, List<Post>>(executors) {
            override fun saveCallResult(item: List<Post>) {
                postDao.insertPosts(item)
            }

            override fun shouldFetch(data: List<Post>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Post>> {
                return postDao.getLatestMinistries(category)
            }

            override fun createCall(): LiveData<ApiResponse<List<Post>>> {
                //return PostService.post.getPosts(per_page)
                return PostService.post.getMinistriesPosts()
            }

        }.asLiveData()
    }

    fun ministryCategories(parentId: Int): Flow<List<Category>> {
        return postDao.getMinistriesByParent(parentId)
    }
}