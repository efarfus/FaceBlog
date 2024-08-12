package com.angellira.eduardoApp.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var id: String = "",
    var img: String = ""
)

