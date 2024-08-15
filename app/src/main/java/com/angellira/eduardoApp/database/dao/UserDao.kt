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

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM user WHERE id = :id")
    fun get(id: String): User?

    @Query("UPDATE User SET name = (:newName) WHERE id IN (:id)")
    fun putName(newName: String, id: String)

    @Query("UPDATE User SET email = (:newEmail) WHERE id IN (:id)")
    fun putEmail(newEmail: String, id: String)

    @Query("UPDATE User SET password = (:newPassword) WHERE id IN (:id)")
    fun putPassword(newPassword: String, id: String)

    @Insert
    fun insertAll(user: List<User>)

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}