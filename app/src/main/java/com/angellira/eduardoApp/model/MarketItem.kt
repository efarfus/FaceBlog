package com.angellira.eduardoApp.model

import kotlinx.serialization.Serializable

@Serializable
data class MarketItem(
    val id: Int,
    val user: User,
    val img: String,
    val price: String,
    val title: String,
    val description: String
)