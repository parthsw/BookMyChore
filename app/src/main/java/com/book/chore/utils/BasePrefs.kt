package com.book.chore.utils

import android.content.Context
import android.content.SharedPreferences
import com.book.chore.ChoreApplication
import java.util.*

object BasePrefs {

    private fun getPrefs(prefName: String): SharedPreferences? {
        return ChoreApplication.appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    fun getEditor(prefName: String): SharedPreferences.Editor? {
        val prefs = getPrefs(prefName)
        return prefs?.edit()
    }

    fun removePref(prefName: String) {
        val editor = getEditor(prefName)
        editor?.clear()
    }

    fun putValue(prefName: String, key: String, value: String) {
        try {
            val editor = getEditor(prefName) ?: return
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
        }

    }

    fun putValue(prefName: String, key: String, value: Long) {
        try {
            val editor = getEditor(prefName) ?: return
            editor.putLong(key, value)
            editor.apply()
        } catch (e: Exception) {
        }

    }

    fun putValue(prefName: String, key: String, value: Int) {
        try {
            val editor = getEditor(prefName) ?: return
            editor.putInt(key, value)
            editor.apply()
        } catch (e: Exception) {
        }

    }

    fun putValue(prefName: String, key: String, value: Boolean) {
        try {
            val editor = getEditor(prefName) ?: return
            editor.putBoolean(key, value)
            editor.apply()
        } catch (e: Exception) {
        }

    }

    fun putValue(prefName: String, key: String, value: Float) {
        try {
            val editor = getEditor(prefName) ?: return
            editor.putFloat(key, value)
            editor.apply()
        } catch (e: Exception) {
        }

    }

    fun putStringSet(prefName: String, key: String, value: Set<String>) {
        try {
            val editor = getEditor(prefName) ?: return
            editor.putStringSet(key, value)
            editor.apply()
        } catch (e: Exception) {
        }

    }

    fun getString(prefName: String, key: String, defaultValue: String? = null): String? {
        val prefs = getPrefs(prefName)
        return prefs?.getString(key, defaultValue)
    }

    fun getLong(prefName: String, key: String, defaultValue: Long = 0): Long {
        val prefs = getPrefs(prefName)
        return prefs?.getLong(key, defaultValue) ?: 0L
    }

    fun getInt(prefName: String, key: String, defaultValue: Int = 0): Int {
        val prefs = getPrefs(prefName)
        return prefs?.getInt(key, defaultValue) ?: 0
    }

    fun getBoolean(prefName: String, key: String, defaultVal: Boolean = false): Boolean {
        val prefs = getPrefs(prefName)
        return prefs?.getBoolean(key, defaultVal) ?: defaultVal
    }

    fun getFloat(prefName: String, key: String, defaultVal: Float = 0f): Float {
        val prefs = getPrefs(prefName)
        return prefs?.getFloat(key, defaultVal) ?: defaultVal
    }

    fun getStringSet(prefName: String, key: String): Set<String> {
        val defaultValue = HashSet<String>()
        val prefs = getPrefs(prefName)
        val value = if (prefs != null) prefs.getStringSet(key, defaultValue) else defaultValue
        val valueCopy = HashSet<String>()
        valueCopy.addAll(value!!)
        return valueCopy
    }

    fun isKey(prefName: String, key: String): Boolean {
        val prefs = getPrefs(prefName)
        return prefs?.contains(key) ?: false
    }

    fun removeKey(prefName: String, key: String) {
        val prefs = getPrefs(prefName)
        prefs?.edit()?.remove(key)?.apply()
    }
}
