package com.angellira.eduardoApp.model

import com.angellira.eduardoApp.Interface.authenticator

data class User(
    var email: String = "",
    var password: String = "",
    var passwordConfirmation: String = "",
    var name: String = ""
) : authenticator {
    override fun authenticate(email: String, password: String): Boolean {
        if (this.email == email && this.password == password) {
            return true
        }
        else
        {
            return false
        }
    }
}
