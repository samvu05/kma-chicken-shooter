package com.example.chickenshooter.utils

import android.content.Context
import android.content.SharedPreferences

open class PreferenceHelper(private val context: Context, name: String) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    // MARK : Background Sound State
    open fun isMute(): Boolean {
        return sharedPreferences.getBoolean("isMute", true)
    }

    open fun setIsMute(newValue: Boolean) {
        sharedPreferences.edit().putBoolean("isMute", newValue).apply()
    }

    // MARK : Highscore
    open fun getHighscore(): Int {
        return sharedPreferences.getInt("highScore", 0)
    }

    open fun setHighscore(newValue: Int) {
        sharedPreferences.edit().putInt("highScore", newValue).apply()
    }

    // MARK : Name
    open fun getName(): String {
        return sharedPreferences.getString("name", "Anonymous")!!
    }

    open fun setName(newValue: String) {
        sharedPreferences.edit().putString("name", newValue).apply()
    }

    // MARK : Top highscore name
    open fun getTopScoreName(): String {
        return sharedPreferences.getString("topScoreName", "s a m v u")!!
    }

    open fun setTopScoreName(newValue: String) {
        sharedPreferences.edit().putString("topScoreName", newValue).apply()
    }

}