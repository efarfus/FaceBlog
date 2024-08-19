package com.angellira.eduardoApp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.angellira.eduardoApp.model.Posts


@Dao
interface PostsDao {
    @Query("SELECT * FROM posts")
    fun getAll(): List<Posts>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun get(id: String): Posts?

    @Insert
    fun insertAll(posts: List<Posts>)

    @Insert
    fun insert(posts: Posts)

    @Update
    fun update(post: Posts)

    @Delete
    fun delete(posts: Posts)
}