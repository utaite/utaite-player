package com.utaite.player.util

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import com.utaite.player.BuildConfig


const val INIT = "INIT"
const val SORTED = "SORTED"


class PreferenceUtil private constructor(context: Context) {

    companion object {

        private var instance: PreferenceUtil? = null

        fun getInstance(context: Context): PreferenceUtil {
            synchronized(this) {
                if (instance == null) {
                    instance = PreferenceUtil(context.applicationContext)
                }
            }

            return instance as PreferenceUtil
        }

    }

    private val name = "${BuildConfig.APPLICATION_ID}.pref"
    private val pref = context.getSharedPreferences(name, AppCompatActivity.MODE_PRIVATE)

    fun setString(key: String, value: String?) =
            pref.put { putString(key, value) }

    fun setBoolean(key: String, value: Boolean) =
            pref.put { putBoolean(key, value) }

    fun setFloat(key: String, value: Float) =
            pref.put { putFloat(key, value) }

    fun setInt(key: String, value: Int) =
            pref.put { putInt(key, value) }

    fun setLong(key: String, value: Long) =
            pref.put { putLong(key, value) }


    fun setStrings(vararg pair: Pair<String?, String?>) {
        for (e in pair) {
            pref.put { putString(e.first, e.second) }
        }
    }

    fun setStrings(map: Map<String?, String?>) {
        for ((key, value) in map) {
            pref.put { putString(key, value) }
        }
    }

    fun setBooleans(vararg pair: Pair<String?, Boolean>) {
        for (e in pair) {
            pref.put { putBoolean(e.first, e.second) }
        }
    }

    fun setBooleans(map: Map<String?, Boolean>) {
        for ((key, value) in map) {
            pref.put { putBoolean(key, value) }
        }
    }

    fun setFloats(vararg pair: Pair<String?, Float>) {
        for (e in pair) {
            pref.put { putFloat(e.first, e.second) }
        }
    }

    fun setFloats(map: Map<String?, Float>) {
        for ((key, value) in map) {
            pref.put { putFloat(key, value) }
        }
    }

    fun setInts(vararg pair: Pair<String?, Int>) {
        for (e in pair) {
            pref.put { putInt(e.first, e.second) }
        }
    }

    fun setInts(map: Map<String?, Int>) {
        for ((key, value) in map) {
            pref.put { putInt(key, value) }
        }
    }

    fun setLongs(vararg pair: Pair<String?, Long>) {
        for (e in pair) {
            pref.put { putLong(e.first, e.second) }
        }
    }

    fun setLongs(map: Map<String?, Long>) {
        for ((key, value) in map) {
            pref.put { putLong(key, value) }
        }
    }


    fun getString(key: String, value: String?): String? =
            try {
                pref.getString(key, value)
            } catch (e: Exception) {
                value
            }

    fun getBoolean(key: String, value: Boolean): Boolean =
            try {
                pref.getBoolean(key, value)
            } catch (e: Exception) {
                value
            }

    fun getFloat(key: String, value: Float): Float =
            try {
                pref.getFloat(key, value)
            } catch (e: Exception) {
                value
            }

    fun getInt(key: String, value: Int): Int =
            try {
                pref.getInt(key, value)
            } catch (e: Exception) {
                value
            }

    fun getLong(key: String, value: Long): Long =
            try {
                pref.getLong(key, value)
            } catch (e: Exception) {
                value
            }


    private inline fun SharedPreferences.put(put: SharedPreferences.Editor.() -> Unit): SharedPreferences =
            apply {
                val editor = edit()
                editor.put()
                editor.apply()
            }

}
