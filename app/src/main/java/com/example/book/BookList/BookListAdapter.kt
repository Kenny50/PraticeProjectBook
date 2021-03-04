package com.example.book.BookList

import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.book.databinding.BookListViewHolderBinding
import com.example.book.room.BookListEntity

class BookListAdapter() : RecyclerView.Adapter<BookListAdapter.BookListViewHolder>() {

    init {
        setHasStableIds(true)
    }
    var data = listOf<NewDataClassWithBoolean>()

    //enable click and data
    var editButtonClickListener: (passData: BookListEntity, posi: Int) -> Unit = { _: BookListEntity, _: Int ->}
    var deleteButtonClick: (selectData: BookListEntity) -> Unit = { b: BookListEntity->}


    // position recode for check hide layout visibility
    var positionRecoder:Int? = null
    //recyclerview.selection
    var tracker: SelectionTracker<Long>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
        val binding = BookListViewHolderBinding.inflate(adapterLayout, parent, false)

        return BookListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        val eachData = data[position]
        holder.bindDataToText(eachData.dataClass)

        initSelectionTracker(holder, position, eachData.dataClass)
        checkIsClicked(holder, position)

        initUpSetOnClick(holder, position, eachData.dataClass)
    }

    override fun getItemId(position: Int): Long = position.toLong()


    override fun getItemCount(): Int { return data.size }




    inner class BookListViewHolder(binding: BookListViewHolderBinding):RecyclerView.ViewHolder(binding.root){
        val title = binding.titleFromData
        val price = binding.priceFromData
        val hide = binding.hideLayout
        val editBtn = binding.editButton
        val deleteBtm = binding.deleteButton

        //return an instance of ItemDetails
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object : ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }

        fun bind(value: Int?, isActivated: Boolean = false) {
            itemView.isActivated = isActivated
        }
        fun bindDataToText(eachData: BookListEntity){
            title.text = eachData.titleFromData
            price.text = eachData.priceFromData.toString()
        }
    }




    private fun initSelectionTracker(holder: BookListViewHolder, position: Int, eachData: BookListEntity){
        tracker?.let {
            Log.i("selectInAdapter",position.toString())
            holder.bind(eachData.id,it.isSelected(position.toLong()))
        }

    }
    private fun checkIsClicked(holder: BookListViewHolder, position: Int){
        if(data[position].isClick && position == positionRecoder && tracker?.selection?.isEmpty == true) {
            holder.hide.visibility = View.VISIBLE
        } else {
            holder.hide.visibility = View.GONE
        }
    }
    private fun initUpSetOnClick(holder: BookListViewHolder, position: Int, eachData:BookListEntity){
        holder.itemView.setOnClickListener {
            Log.i("selectView",it.toString())
            collapsed()
            if (position != positionRecoder) expanded(position) else positionRecoder = null
        }
        holder.deleteBtm.setOnClickListener { deleteButtonClick(eachData)}
        holder.editBtn.setOnClickListener { editButtonClickListener(eachData, position)}
        // Log.i("focus", (it.context as? MainActivity)?.window?.currentFocus.toString())

    }
    private fun collapsed(){
        positionRecoder?.let {
            data[it].isClick = false
            notifyDataSetChanged()
//            notifyItemChanged(it)
        }
    }
    private fun expanded(position: Int){
        data[position].isClick = true
        positionRecoder = position
//        notifyItemChanged(position)
        notifyDataSetChanged()
    }
}

data class NewDataClassWithBoolean(
        val dataClass:BookListEntity,
        var isClick:Boolean = false
)
