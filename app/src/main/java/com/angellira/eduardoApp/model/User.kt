package com.angellira.eduardoApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class User(

    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var img: String = ""
)

