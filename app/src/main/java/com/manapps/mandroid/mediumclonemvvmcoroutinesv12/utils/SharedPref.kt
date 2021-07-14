package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils

import android.content.Context
import android.content.SharedPreferences
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.login.LoginActivity

object SharedPref {

    private val SharedPrefName: String = "SharedPrefMediumApp"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE)
    }

    fun saveUserData(context: Context, key: String?, value: String?) {
        val pref = getSharedPreferences(context)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getSavedUserData(context: Context?, key: String?): String? {
        if (context != null) {
            val pref = getSharedPreferences(context)
            return pref.getString(key, "")
        }
        return ""
    }

    fun logoutSession(context: Context) {
        val pref = getSharedPreferences(context).edit()
        pref.clear().apply()
    }
    fun logoutSessionAndGoToLogin(context: Context) {
        val pref = getSharedPreferences(context).edit()
        pref.clear().apply()
        Utils.moveToAndClearHisory(context, LoginActivity::class.java)
    }

}

