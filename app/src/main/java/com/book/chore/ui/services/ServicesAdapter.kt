package com.book.chore.ui.services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.book.chore.R
import com.book.chore.data.Doer.ChoreDoer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.task_doer_details.view.*

class ServicesAdapter(
    private val context: Context,
    private val posters: List<ChoreDoer>,
    private val itemClickListener: OnTaskDoerItemClickListener
) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private var listener: OnTaskDoerItemClickListener? = null

    init {
        this.listener = itemClickListener
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.task_doer_details, viewGroup, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = posters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = posters[position]
        //Glide is used to load an image using a url
        Glide.with(context).load(user.userProfilePic).into(holder.profileImg)
        holder.nameTxt.text = user.userDisplayName
        holder.RValue.text = user.rating
        holder.rate.text = user.hourlyRate
//        holder.phoneNumber.text = user.userMobile
        holder.description.text = user.userDescription

        holder.itemView.setOnClickListener { listener!!.onItemClick(user) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView
            get() = itemView.doerImg
        val nameTxt: TextView
            get() = itemView.doerName
        val RValue: TextView
            get() = itemView.rating
        val rate: TextView
            get() = itemView.hourlyRate
        val description: TextView
            get() = itemView.description
//        val phoneNumber: TextView
//            get() = itemView.phoneNumber

    }

}

interface OnTaskDoerItemClickListener {
    fun onItemClick(item: ChoreDoer?)
}



