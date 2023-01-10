package com.example.lastmailattendent.util.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val USER_TOKEN = "token"

private const val USER_ID="id"

class PreferenceProvider(
    context: Context
){

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun setToken(token: String,id:String) {
      preference.edit().putString(USER_TOKEN, token).apply()
        preference.edit().putString(USER_ID,id).apply()
    }

    fun getToken(): String? {
        return preference.getString(USER_TOKEN, null)
    }
    fun getUser(): String? {
        return preference.getString(USER_ID, null)
    }
}