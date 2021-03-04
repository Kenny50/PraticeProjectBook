package com.example.book.BookList

import android.util.Log
import androidx.lifecycle.*
import com.example.book.application.DataApplication
import com.example.book.room.BookListEntity
import com.example.book.room.DatabaseDao
import kotlinx.coroutines.*

class BookListFragmentViewModel() :ViewModel(){
    val selections = MutableLiveData<List<Int>>()
    val useDao: DatabaseDao = DataApplication.getDataBase.bookDatabaseDao()

    private val _bookData = MutableLiveData<List<BookListEntity>>()
    val bookData:LiveData<List<BookListEntity>>
        get() = _bookData

    var clickData = BookListEntity(null, -1, "")

    fun resetClickData(id:Int? = null, price:Int = -1, title:String = ""){
        clickData = BookListEntity(id, price, title)
    }

//    val bookData: LiveData<List<BookListEntity>> = liveData { emit(useDao.getAll()}
//    val bookData: LiveData<List<BookListEntity>> = useDao.getAll


    fun generateRandomData(){
        val charString = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val tmpString = charString.shuffled().take((5..15).random()).toString().replace(",|\\p{Punct}|\\s".toRegex(),"")
        val fakeData = BookListEntity(null,(1..9999).random(),tmpString)
        viewModelScope.launch {
            addNewBook(fakeData)
        }
    }

    fun showAllData(){
        try {
            viewModelScope.launch {
                _bookData.value = useDao.getAll()
                bookData
                Log.i("sharedViewModel","get data successful")
            }
        }catch (e:Exception){
            Log.i("sharedViewModel","kaboom " + e.message)
        }
    }
    fun addNewBook(newData:BookListEntity){
        viewModelScope.launch {
            useDao.insertNewHistory(newData)
            showAllData()
        }
    }
    fun deleteAll(){
        viewModelScope.launch {
            useDao.deleteAll()
            showAllData()
        }
    }
    fun update(b:BookListEntity){
        viewModelScope.launch {
            useDao.updateBook(b)
        }
        showAllData()
    }
    fun deleteOne(b:BookListEntity){
        viewModelScope.launch {
            useDao.deleteBook(b)
        }
        showAllData()
    }
    fun findBook(s:String){
        try {
            viewModelScope.launch {
                _bookData.value = useDao.findBookTitle("%$s%")
                Log.i("sharedViewModel","get data successful")
            }
        }catch (e:Exception){
            Log.i("sharedViewModel","kaboom " + e.message)
        }
    }
    private fun deleteMultiple(num:List<Int>){
        viewModelScope.launch {
            useDao.deleteMultiple(num)
        }
        showAllData()
    }

    fun setSelections(items: List<Int>) {
        selections.value = items

    }

    fun deleteSelections() {
        viewModelScope.launch {
            Log.d("VM",selections.value.toString())
            selections.value?.let { deleteMultiple(it) }
            selections.value = emptyList()
        }
    }
}