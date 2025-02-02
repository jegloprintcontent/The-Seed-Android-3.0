package org.ileewe.theseedjcli.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import org.ileewe.theseedjcli.dao.PostDao
import org.ileewe.theseedjcli.executor.AppExecutors
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.requests.PostService
import org.ileewe.theseedjcli.requests.response.ApiResponse
import org.ileewe.theseedjcli.utils.Constants.DEVOTIONS_CATEGORY
import org.ileewe.theseedjcli.utils.Constants.MINISTRIES_CATEGORY
import org.ileewe.theseedjcli.utils.Constants.SERMONS_CATEGORY
import org.ileewe.theseedjcli.utils.NetworkBoundResource
import org.ileewe.theseedjcli.utils.Resource

class HomeRepository(private val postDao: PostDao, private val executors: AppExecutors) {

    @WorkerThread
    suspend fun insertPosts(post: List<Post>) {
        return postDao.insertPosts(post)
    }

    fun getPosts(per_page: Int): LiveData<Resource<List<Post>>> {
        return object: NetworkBoundResource<List<Post>, List<Post>>(executors) {
            override fun saveCallResult(item: List<Post>) {
                postDao.insertPosts(item)
            }

            override fun shouldFetch(data: List<Post>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Post>> {
                return postDao.getLatestDevotions(DEVOTIONS_CATEGORY)
            }

            override fun createCall(): LiveData<ApiResponse<List<Post>>> {
                //return PostService.post.getPosts(per_page)
                return PostService.post.getPosts()
            }

        }.asLiveData()


    }


    fun getMinistries(parentId: Int): LiveData<Resource<List<Category>>> {
        return object: NetworkBoundResource<List<Category>,List<Category>>(executors) {
            override fun saveCallResult(item: List<Category>) {
                postDao.insertMinistries(item)
            }

            override fun shouldFetch(data: List<Category>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Category>> {
                return postDao.getAllMinistries()
            }

            override fun createCall(): LiveData<ApiResponse<List<Category>>> {
                //return PostService.category.getCategoriesByParentId(parentId)
                return PostService.category.getCategoriesByParentId()
            }

        }.asLiveData()
    }


//    fun sermonPosts(category_id: Int): Flow<List<Post>> {
//        return postDao.getSermons(category_id)
//    }

    fun getSermonPost(): LiveData<Resource<List<Post>>> {
        return object: NetworkBoundResource<List<Post>, List<Post>>(executors) {
            override fun saveCallResult(item: List<Post>) {
                postDao.insertPosts(item)
            }

            override fun shouldFetch(data: List<Post>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<Post>> {

                return postDao.getSermons(SERMONS_CATEGORY)
                //return postDao.getLatestDevotions(DEVOTIONS_CATEGORY)
            }

            override fun createCall(): LiveData<ApiResponse<List<Post>>> {
                //return PostService.post.getPosts(per_page)
                return PostService.post.getSermons()
            }

        }.asLiveData()

    }

}