package com.example.book.application

import android.app.Application
import com.example.book.room.BookDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DataApplication:Application() {
    companion object{
        lateinit var getDataBase: BookDatabase
        lateinit var instanceApp:Application
    }

    override fun onCreate() {
        super.onCreate()
        instanceApp = this
        val ApplicationJob = Job()
        val ApplicationScope = CoroutineScope(Dispatchers.Default + ApplicationJob)

        ApplicationScope.launch {
            getDataBase = BookDatabase.getDatabase(applicationContext)
        }
    }
}