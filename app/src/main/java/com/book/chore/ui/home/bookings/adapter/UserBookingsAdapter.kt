package com.book.chore.ui.home.bookings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.book.chore.R
import com.book.chore.data.User.UserBookings
import com.book.chore.data.User.UserHistory
import com.book.chore.ui.home.bookings.BookingsFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.bookingscardview.view.*

class UserBookingsAdapter(
    private val context: BookingsFragment,
    private val posters: List<UserHistory>
) : RecyclerView.Adapter<UserBookingsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.bookingscardview, viewGroup, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = posters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = posters[position]
        //Glide is used to load an image using a url
        Glide.with(context).load(user.profileurl).into(holder.profileImg)
        holder.nameTxt.text = user.Name
        holder.Dvalue.text = user.Date
        holder.Dcost.text = user.totalCost
        holder.DserviceName.text = user.ServiceName
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImg: ImageView
            get() = itemView.profileImg
        val nameTxt: TextView
            get() = itemView.nameTxt
        val Dvalue: TextView
            get() = itemView.Dvalue
        val Dcost: TextView
            get() = itemView.Dcost
        val DserviceName: TextView
            get() = itemView.DserviceName

    }

}