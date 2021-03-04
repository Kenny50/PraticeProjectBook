package com.example.book.BookList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.book.MainActivity
import com.example.book.R
import com.example.book.databinding.FragmentAddBookBtmSheetBinding
import com.example.book.room.BookListEntity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddBookBtmSheet: BottomSheetDialogFragment() {
    private val viewModel: BookListFragmentViewModel by activityViewModels()
    lateinit var binding:FragmentAddBookBtmSheetBinding
    lateinit var packageDataAsBookData:BookListEntity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_add_book_btm_sheet, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (id, price, title)  = viewModel.clickData

        //if id is not null, means this is edit data, so put text
        if (id != null) putText(price, title)

        //if editData, change it, else insert new data
        setUpSetOnClickListener(id)
    }

    private fun putText(p:Int, t:String){
        binding.priceEnterBox.setText(p.toString())
        binding.titleEnterPrompt.setText(t)
    }

    private fun updateOrInsert(b:BookListEntity){
        viewModel.update(b)
        viewModel.resetClickData()
    }
    private fun updateOrInsert(i:Int,s:String){
        packageDataAsBookData = BookListEntity(null, i,s)
        viewModel.addNewBook(packageDataAsBookData)
    }

    private fun setUpSetOnClickListener(id:Int?){
        binding.bottomSheetSentButton.setOnClickListener {
            if (id != null){
                packageDataAsBookData = BookListEntity(id, binding.priceEnterBox.text.toString().toInt(),binding.titleEnterPrompt.text.toString())
                updateOrInsert(packageDataAsBookData)
            } else {
                updateOrInsert(binding.priceEnterBox.text.toString().toInt(),binding.titleEnterPrompt.text.toString())
            }
            this.dismiss()
        }
        binding.bottomSheetCancelButton.setOnClickListener{ this.dismiss() }
    }
}