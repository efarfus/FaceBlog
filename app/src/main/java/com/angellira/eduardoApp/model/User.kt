package com.angellira.eduardoApp.model

import com.angellira.eduardoApp.Interface.authenticator
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var passwordConfirmation: String = ""
) : authenticator {
    override fun authenticate(email: String, password: String): Boolean {
        if (this.email == email && this.password == password) {
            return true
        } else {
            return false
        }
    }
}
