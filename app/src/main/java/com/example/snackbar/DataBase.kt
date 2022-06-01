package com.example.snackbar

import androidx.room.*

@Database(entities = [Weather::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun weatherDao(): DAO
}