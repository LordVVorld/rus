package com.example.snackbar

import android.app.Application
import androidx.room.Room

class AccompaniedApplication : Application() {
    companion object {
        lateinit var db: DataBase
        lateinit var api: RetrofitServices
    }

    override fun onCreate() {
        super.onCreate()

        api = Common.retrofitService
        db = Room.databaseBuilder(applicationContext, DataBase::class.java, "db").build()
    }
}