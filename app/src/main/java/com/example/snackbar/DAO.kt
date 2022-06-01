package com.example.snackbar

import androidx.room.*

@Dao
interface DAO {
    @Query("SELECT * FROM Weather")
    suspend fun getAll(): List<Weather>

    @Insert
    suspend fun insert(vararg weather: Weather)
}