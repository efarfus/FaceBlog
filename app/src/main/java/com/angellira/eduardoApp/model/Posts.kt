package com.angellira.eduardoApp.model

import kotlinx.serialization.Serializable

@Serializable
data class Posts(
    val id: String,
    val user: String,
    val message: String,
    val img: String
)