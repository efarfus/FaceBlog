package com.angellira.eduardoApp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angellira.eduardoApp.model.MarketItem

@Dao
interface MarketItemDao {
    @Query("SELECT * FROM marketitem")
    fun getAll(): List<MarketItem>

    @Query("SELECT * FROM marketitem WHERE id = :id")
    fun get(id: String): MarketItem?

    @Insert
    fun insertAll(marketItem: List<MarketItem>)

    @Insert
    fun insert(marketItem: MarketItem)

    @Delete
    fun delete(marketItem: MarketItem)
}