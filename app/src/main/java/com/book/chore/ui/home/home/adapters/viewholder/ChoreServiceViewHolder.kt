package com.book.chore.ui.home.home.adapters.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.book.chore.data.ChoreServices.ChoreService
import com.book.chore.databinding.ViewHolderChoreServiceBinding
import com.bumptech.glide.Glide

class ChoreServiceViewHolder(val binder: ViewHolderChoreServiceBinding) :
    RecyclerView.ViewHolder(binder.root) {
    fun bind(choreService: ChoreService) {
        with(binder) {
            Glide.with(itemView).load(choreService.serviceIcon).into(imgServiceIcon)
            txtServiceName.text = choreService.serviceName
        }
    }
}