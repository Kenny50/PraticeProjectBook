package com.example.book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.book.BookList.BookListFragment
import com.example.book.BookList.BookListFragmentViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: BookListFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(BookListFragmentViewModel::class.java)
//
//        val manager = supportFragmentManager
//        val transaction = manager.beginTransaction()
//        transaction.replace(R.id.fragment_holder, BookListFragment()).commit()
    }
}