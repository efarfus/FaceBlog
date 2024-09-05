package com.angellira.eduardoApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class MarketItem(
    @PrimaryKey (autoGenerate = true)
    var id: Long = 0,
    var user: String = "",
    var img: String = "",
    var price: String = "",
    var title: String = "",
    var description: String = ""
)