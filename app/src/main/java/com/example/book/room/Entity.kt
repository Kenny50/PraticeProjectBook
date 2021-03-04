package com.example.book.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bookshelf")
data class BookListEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    var priceFromData:Int,
    var titleFromData:String
)