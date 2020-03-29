package com.book.chore.ui.home.help

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.util.Log.i
import android.widget.Button
import android.widget.EditText
import android.widget.ExpandableListAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.book.chore.ChoreApplication
import com.book.chore.R
import com.book.chore.data.User.ChoreUser
import com.book.chore.data.User.UserManager
import com.book.chore.utils.BasePrefs
import com.book.chore.utils.ChoreConstants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_help_center.*
import kotlinx.android.synthetic.main.dialog_ask_que.*

class Faq_activity : AppCompatActivity() {

    private var user: ChoreUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_help_center)

        title="FAQ"
        var obj=Faq_data()

      var id= ChoreConstants.Parameters.userEmail




       val header=obj.question_related_to_booking()
        val body=obj.answer_related_to_booking()
        expandableview.setAdapter( Faq_list_adapter(this,expandableview,header,body))

       val payment_header=obj.question_related_to_payment()
      val payment_body=obj.answer_related_to_payment()
        expandableview0.setAdapter( Faq_list_adapter(this,expandableview0,payment_header,payment_body))

        val btn=findViewById<Button>(R.id.btn_submit_question)
        btn.setOnClickListener {
            val dial= android.app.AlertDialog.Builder(this)
            val dialogview=layoutInflater.inflate(R.layout.dialog_ask_que,null)
            val btn_submit=dialogview.findViewById<Button>(R.id.submit_query)
            val ed_query=dialogview.findViewById<EditText>(R.id.ed_user_query_dialog)
            dial.setView(dialogview)

           val dialog= dial.show()
            btn_submit.setOnClickListener {
                var name= BasePrefs.getString(
                    ChoreConstants.PrefNames.PREF_NAME_USER,
                    ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN_USER_ID
                ).toString()
                var query=ed_query.text.toString()
                if(query.isEmpty())
                {
                    Toast.makeText(applicationContext,"Please enter you query",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else {
                    val data = hashMapOf(
                        "query" to query,
                        "user_id" to name
                    )
                    FirebaseFirestore.getInstance().collection("user_query").add(data)
                        .addOnSuccessListener {

                            dialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                "Thank you. We will try to answer it as soon as possible.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                        Toast.makeText(applicationContext, "Sorry. ", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }


    }
}
