package com.book.chore.data.Doer

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.book.chore.R
import com.book.chore.ui.services.ConfirmBookingFragment
import com.book.chore.ui.services.OnTaskDoerItemClickListener
import com.book.chore.ui.services.SFHolder
import com.book.chore.ui.services.ServicesAdapter
import com.google.common.eventbus.EventBus
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TDManager{
    fun retrieveData(
        context: Context,
        recyclerView: RecyclerView,
        city: String
    ) {
        val ref = FirebaseDatabase.getInstance().getReference("task_doer")
        val doerList = mutableListOf<ChoreDoer>()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error getting data", Toast.LENGTH_LONG).show()
                Log.i("Error: ",p0.message)
                Log.i("Error: ",p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    for (pika in p0.children) {

                        val pikadata = pika.getValue(ChoreDoer::class.java)
                        if((city.isNotEmpty()|| city.isNotBlank()) && pikadata?.city!=city){
                            continue
                        }
                        doerList.add(pikadata!!)
                        Log.i("TAG", "output${pikadata!!.rating.toString()}")
                        Log.i("TAG", "output${doerList.size.toString()}")
                    }
                    recyclerView.adapter = ServicesAdapter(context, doerList, object :
                        OnTaskDoerItemClickListener {
                        override fun onItemClick(item: ChoreDoer?) {
                            if (item != null) {
                                Toast.makeText(context, item.userDisplayName, Toast.LENGTH_LONG).show()
//                                val fragment = ConfirmBookingFragment()
//                                fragment.arguments = intent.extras
//                                supportFragmentManager.beginTransaction()
//                                    .add(R.id.task_doers_details,fragment)
//                                    .commit()
                                val intent = Intent(context, SFHolder::class.java)
                                context.startActivity(intent)

                                //EventBus.getDefault().post(UserCreationEvent())

                            }
                        }
                    })
                }
                else{
                    Toast.makeText(context, "No Task Doer Available for your location", Toast.LENGTH_LONG).show()
                }
            }

        })
    }
}