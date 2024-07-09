package com.angellira.eduardoApp.Interface

interface authenticator {
    val isLocked: Boolean get() = false
    fun authenticate(email: String, password: String) : Boolean

}