package com.angellira.eduardoApp.preferences

import android.content.Context

const val DB_NAME = "userPreferences"

class Preferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)

    var name: String?
        get() = sharedPreferences.getString("name", null)
        set(value) {
            sharedPreferences.edit().putString("name", value).apply()
        }

    var id: String?
        get() = sharedPreferences.getString("id", null)
        set(value) {
            sharedPreferences.edit().putString("id", value).apply()
        }

    var email: String?
        get() = sharedPreferences.getString("email", null)
        set(value) {
            sharedPreferences.edit().putString("email", value).apply()
        }

    var password: String?
        get() = sharedPreferences.getString("password", null)
        set(value) {
            sharedPreferences.edit().putString("password", value).apply()
        }

    var isLogged: Boolean
        get() = sharedPreferences.getBoolean("isLogged", false)
        set(value) {
            sharedPreferences.edit().putBoolean("isLogged", value).apply()
        }

    fun clear(){
        sharedPreferences.edit().clear().apply()
    }

}
