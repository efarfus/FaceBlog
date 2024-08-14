package com.angellira.eduardoApp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.PostsDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.model.MarketItem
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User

@Database(entities = [User::class, Posts::class, MarketItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postsDao(): PostsDao
    abstract fun marketItemDao(): MarketItemDao
}