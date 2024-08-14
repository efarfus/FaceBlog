package com.angellira.eduardoApp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angellira.eduardoApp.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun get(id: String): User?

    @Insert
    fun insertAll(user: List<User>)

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}