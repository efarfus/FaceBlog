package com.angellira.eduardoApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(

    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var img: String = ""
)

