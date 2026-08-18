package com.hoardapp

import android.app.Application
import com.hoardapp.data.AppDatabase
import com.hoardapp.data.HoardRepository

class HoardApplication : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { HoardRepository(database) }
}
