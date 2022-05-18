package com.prography.pilit.data.datasource.local

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString("accessToken",null)
        set(value) = prefs.edit().putString("accessToken",value).apply()

    var nickname: String?
        get() = prefs.getString("nickname",null)
        set(value) = prefs.edit().putString("nickname",value).apply()

    fun clearPreferences(){
        prefs.edit().clear().apply()
    }
}