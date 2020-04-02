package com.book.chore.ui.home.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.book.chore.data.ChoreServices.ChoreService
import com.book.chore.databinding.ViewHolderChoreServiceBinding
import com.book.chore.ui.home.home.adapters.viewholder.ChoreServiceViewHolder

class ChoreServicesAdapter(private val choreServicesList: MutableList<ChoreService>, private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<ChoreServiceViewHolder>() {
    private var listener: OnItemClickListener? = null

    init {
        this.listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreServiceViewHolder {
        return ChoreServiceViewHolder(
            ViewHolderChoreServiceBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return choreServicesList.size
    }

    override fun onBindViewHolder(holder: ChoreServiceViewHolder, position: Int) {




        holder.bind(choreServicesList[position], listener)
    }
}

interface OnItemClickListener {
    fun onItemClick(item: ChoreService?)
}