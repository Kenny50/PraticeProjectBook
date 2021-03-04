package com.example.book.BookList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.book.R
import com.example.book.databinding.FragmentBookListBinding
import com.example.book.room.BookListEntity

class BookListFragment : Fragment() {
    lateinit var binding: FragmentBookListBinding
    lateinit var recyclerView: RecyclerView
    private val adapter: BookListAdapter = BookListAdapter()
    val viewModel: BookListFragmentViewModel by activityViewModels()
    lateinit var ftracker:SelectionTracker<Long>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_book_list, container, false)

        recyclerView = binding.bookListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = adapter
        viewModel.showAllData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSelectTracker()

        //check selections, if has selected, show button and size
        initSelectionObserver()
        //observer data from room
        initBookDataObserver()

        initAdapterButtonOnClickCallBack()


        initFragmentButtonOnClick()

        setSearchViewMethod()

    }


    private fun setSelectTracker(){
        ftracker = SelectionTracker.Builder<Long>(
                "mySelection",
                recyclerView,
                StableIdKeyProvider(recyclerView),
                SelectItemDetailLookUp(recyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()
        adapter.tracker = ftracker

        adapter.tracker?.addObserver(
                object : SelectionTracker.SelectionObserver<Long>() {
                    override fun onSelectionChanged() {
                        super.onSelectionChanged()
                        adapter.tracker?.let {
                            val items : Selection<Long> = it.selection
                            Log.i("select", items.toString())
                            //save select data id
                            items.toList().map { position -> adapter.data[position.toInt()].dataClass.id as Int }.let { id->
                                viewModel.setSelections(id)
                            }
                        }
                    }
                }
        )

    }

    private fun initSelectionObserver(){
        //observer select id
        viewModel.selections.observe(viewLifecycleOwner, Observer { items->
            if (items.isEmpty()) {
                adapter.tracker?.clearSelection()
                binding.deleteMultiple.visibility = View.GONE
                viewModel.showAllData()
            } else {
                binding.deleteMultiple.visibility = View.VISIBLE
                binding.deleteMultiple.text = getString(R.string.test, items.size)
            }
        })
    }

    private fun initBookDataObserver(){
        viewModel.bookData.observe(viewLifecycleOwner, Observer {
            adapter.data = it.map { bookListEntityDataClass -> NewDataClassWithBoolean(bookListEntityDataClass) }
            adapter.notifyDataSetChanged()

        })
    }
    private fun initAdapterButtonOnClickCallBack(){
        adapter.apply {
            deleteButtonClick = { b: BookListEntity -> viewModel.deleteOne(b) }
            editButtonClickListener = { clickedData: BookListEntity, clickPosition: Int ->
                val (id,price,title) = clickedData
                viewModel.resetClickData(id,price,title)
                AddBookBtmSheet().show(childFragmentManager, "btmDialogOpen")
            }
        }
    }

    private fun initFragmentButtonOnClick(){
        binding.deleteMultiple.setOnClickListener {
            viewModel.deleteSelections()
        }
        // for insert new data, open bottom sheet with empty edit text
        binding.openBottomSheet.setOnClickListener {
            viewModel.resetClickData()
            AddBookBtmSheet().show(childFragmentManager, "btmDialogOpen")
        }
        //generate random data
        binding.openBottomSheet.setOnLongClickListener {
            repeat(10){ viewModel.generateRandomData() }
            Toast.makeText(this.requireContext(),"add 10 data into dataset",Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }
    }

    private fun setSearchViewMethod(){
        binding.searchBook.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.findBook(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) viewModel.showAllData()
                return false
            }
        })
    }
}