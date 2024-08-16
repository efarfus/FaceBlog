package com.angellira.eduardoApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Posts(
    @PrimaryKey
    val id: String = "",
    val user: String = "",
    val message: String = "",
    val img: String = ""
)