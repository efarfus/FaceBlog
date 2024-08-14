package com.angellira.eduardoApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Posts(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    val user: String,
    val message: String,
    val img: String
)