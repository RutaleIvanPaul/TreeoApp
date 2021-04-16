package org.fairventures.treeo.util

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener


class PrefManager {
    fun persist(context: Context, language: String?) {
        val preferences = context.getSharedPreferences("Choose Language", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("language", language)
        editor.apply()
    }

    fun getPersistStorage(context: Context): String? {
        val preferences = context.getSharedPreferences("Choose Language", Context.MODE_PRIVATE)
        return preferences.getString("language", "")
    }

}
