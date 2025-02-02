package org.ileewe.theseedjcli.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.ileewe.theseedjcli.model.Category
import org.ileewe.theseedjcli.model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(post: List<Post>)

    @Query("SELECT * FROM posts ORDER BY date DESC LIMIT 6")
    fun getLatestPost(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE categories LIKE '[' || :category_id || ',%' " +
            "OR categories LIKE '[' || :category_id || ']' " +
            " OR categories LIKE '%,' || :category_id || ']' ORDER BY date DESC LIMIT 20")
    fun getLatestSeeds(category_id: Int): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE categories LIKE '[' || :category_id || ',%' " +
            "OR categories LIKE '[' || :category_id || ']' " +
            " OR categories LIKE '%,' || :category_id || ']' ORDER BY date DESC LIMIT 6")
    fun getLatestDevotions(category_id: Int): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE categories LIKE '[' || :category_id || ',%' " +
            "OR categories LIKE '[' || :category_id || ']' " +
            " OR categories LIKE '%,' || :category_id || ']' ORDER B" +
            "" +
            "" +
            "" +
            "Y date DESC LIMIT 5")
    fun getSermons(category_id: Int): LiveData<List<Post>>
    //fun getSermons(category_id: Int): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE categories LIKE '[' || :category_id || ',%' " +
            "OR categories LIKE '[' || :category_id || ']' " +
            " OR categories LIKE '%,' || :category_id || ']' ORDER BY date DESC LIMIT 20")
    fun getLatestSermons(category_id: Int): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE categories LIKE '[' || :category_id || ',%' " +
            "OR categories LIKE '[' || :category_id || ']' " +
            " OR categories LIKE '%,' || :category_id || ']' ORDER BY date DESC LIMIT 20")
    fun getLatestMinistries(category_id: Int): LiveData<List<Post>>


    //Categories
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insertMinistries(category: List<Category>)

    @Query("SELECT * FROM categories ORDER BY id DESC LIMIT 5")
    fun getAllMinistries(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE parent = :parent_id ORDER BY id DESC LIMIT 20")
    fun getMinistriesByParent(parent_id: Int): Flow<List<Category>>

}