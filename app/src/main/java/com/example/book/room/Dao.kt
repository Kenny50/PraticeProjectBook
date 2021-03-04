package com.example.book.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DatabaseDao {

//    @get:Query("SELECT * FROM bookshelf")
//    val getAll:LiveData<List<BookListEntity>>
    @Query("SELECT * FROM bookshelf")
    suspend fun getAll():List<BookListEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewHistory(newBookData:BookListEntity)

    @Query("DELETE FROM bookshelf")
    suspend fun deleteAll()

    @Update
    suspend fun updateBook(vararg book:BookListEntity)

    @Delete
    suspend fun deleteBook(vararg book:BookListEntity)

    @Query("SELECT * FROM bookshelf WHERE titleFromData Like :search")
    suspend fun findBookTitle(search:String):List<BookListEntity>

    @Query("DELETE FROM bookshelf WHERE id IN (:num) ")
    suspend fun deleteMultiple(num:List<Int>)
}