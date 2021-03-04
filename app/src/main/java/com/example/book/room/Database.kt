package com.example.book.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BookListEntity::class), version = 1, exportSchema = false)
abstract class BookDatabase:RoomDatabase() {
    abstract fun bookDatabaseDao() :DatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: BookDatabase? = null


        fun getDatabase(context:Context):BookDatabase{
            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_detail_database"
                ).build()

                INSTANCE = instance
                return  instance
            }
        }
    }
}

// from others note        val db = AppDatabase(this)
//volatile可以預防在cpu的多執行緒時，產生一個worker thread半成品的class和一個main thread完成品的Classss
//synchronized可能會產生double null exception