package com.angellira.eduardoApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketItem(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    var user: String = "",
    var img: String = "",
    var price: String = "",
    var title: String = "",
    var description: String = ""
)