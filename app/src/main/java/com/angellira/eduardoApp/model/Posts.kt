package com.angellira.eduardoApp.model

import kotlinx.serialization.Serializable

@Serializable
data class Posts(
    val id: Int,
    val user: User,
    val message: String,
    val img: String
)