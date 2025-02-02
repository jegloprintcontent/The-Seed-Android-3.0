package org.ileewe.theseedjcli.requests

import androidx.lifecycle.LiveData
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.requests.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryAPI {

//    @GET("wp-json/wp/v2/categories")
//    fun getCategoriesByParentId(@Query ("parent") id: Int ): LiveData<ApiResponse<List<Category>>>

    @GET("v1/list-ministries.json")
    fun getCategoriesByParentId(): LiveData<ApiResponse<List<Category>>>

}