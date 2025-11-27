package com.app.moviltest.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.app.moviltest.data.local.model.SudokuCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuPreferences @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PreferencesConstants.PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun saveSudokuGame(
        puzzle: List<List<Int?>>,
        solution: List<List<Int?>>,
        currentBoard: List<List<Int?>>
    ) {
        prefs.edit()
            .putString(PreferencesConstants.KEY_SUDOKU_CACHE, gson.toJson(
                SudokuCache(
                    puzzle = gson.toJson(puzzle),
                    solution = gson.toJson(solution),
                    currentBoard = gson.toJson(currentBoard),
                    timestamp = System.currentTimeMillis()
                )
            ))
            .putLong(PreferencesConstants.KEY_LAST_UPDATE, System.currentTimeMillis())
            .apply()
    }

    fun getSudokuCache(): SudokuCache? {
        val json = prefs.getString(PreferencesConstants.KEY_SUDOKU_CACHE, null)
        if (json == null) return null

        return try {
            gson.fromJson(json, SudokuCache::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun deserializePuzzle(json: String): List<List<Int?>> {
        val type = object : TypeToken<List<List<Int?>>>() {}.type
        return gson.fromJson(json, type)
    }

    fun isCacheValid(): Boolean {
        val lastUpdate = prefs.getLong(PreferencesConstants.KEY_LAST_UPDATE, 0)
        return System.currentTimeMillis() - lastUpdate < PreferencesConstants.CACHE_DURATION
    }

    fun clearCache() {
        prefs.edit().clear().apply()
    }
}