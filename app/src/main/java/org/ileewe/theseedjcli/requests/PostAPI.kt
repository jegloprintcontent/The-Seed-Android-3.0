package org.ileewe.theseedjcli.requests

import androidx.lifecycle.LiveData
import org.ileewe.theseedjcli.model.Post
import org.ileewe.theseedjcli.requests.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PostAPI {

//    @GET("wp-json/wp/v2/posts")
//    fun getPosts(@Query("per_page") per_page: Int): LiveData<ApiResponse<List<Post>>>


    @GET("v1/latest-seeds.json")
    fun getPosts(): LiveData<ApiResponse<List<Post>>>

    @GET("v1/latest-sermons.json")
    fun getSermons(): LiveData<ApiResponse<List<Post>>>

    @GET("v1/latest-ministries-posts.json")
    fun getMinistriesPosts(): LiveData<ApiResponse<List<Post>>>

}