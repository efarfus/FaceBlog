package com.angellira.eduardoApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketItem(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    val user: String,
    val img: String,
    val price: String,
    val title: String,
    val description: String
)