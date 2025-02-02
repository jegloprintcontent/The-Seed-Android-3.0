//package org.ileewe.theseedjcli.audioApi
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//
//data class Post(
//    val id: Int,
//    val title: String,
//    val content: String,
//    val audioUrl: String // Add audio URL field
//)
//
//interface WordPressApi {
//    @GET("wp-json/wp/v2/posts")
//    suspend fun getPosts(): List<Post>
//}
//
//val retrofit: Retrofit = Retrofit.Builder()
//    .baseUrl("https://theseedjcli.ileewe.org/")
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()
//
//val api: WordPressApi = retrofit.create(WordPressApi::class.java)
