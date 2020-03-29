package com.book.bookmy.ui.login.home.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.book.chore.R
import com.book.chore.ui.home.help.About_us
import com.book.chore.ui.home.help.Faq_activity
import com.book.chore.ui.home.help.HelpViewModel
import com.book.chore.utils.BasePrefs
import com.book.chore.utils.ChoreConstants
import com.google.firebase.firestore.FirebaseFirestore


class HelpFragment : Fragment() {


    private lateinit var helpViewModel: HelpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_help, container, false)

        val btnhelp: Button =root.findViewById(R.id.btnhelp)
        val btnabout: Button =root.findViewById(R.id.btnabout)
        val btnrate: Button =root.findViewById(R.id.btnrating)

        btnhelp.setOnClickListener {
            val intent = Intent(activity, Faq_activity::class.java)
            startActivity(intent)
        }
        btnabout.setOnClickListener {
            val intent = Intent(activity, About_us::class.java)
            startActivity(intent)
        }
        btnrate.setOnClickListener {

            val dial= AlertDialog.Builder(activity)
            val dialogview=layoutInflater.inflate(R.layout.dialog_rating,null)
            // val rating_val=dialoview.findViewById<RatingBar>(R.id.ratingbar).rating
            val btn_submit=dialogview.findViewById<Button>(R.id.btn_submit_rating)
            //Toast.makeText(activity,"Thank you for giving us "+rating_val+" star",Toast.LENGTH_SHORT).show()
            dial.setView(dialogview)
            val dialog= dial.show()


            btn_submit.setOnClickListener {
                val rating_val=dialogview.findViewById<RatingBar>(R.id.ratingbar).rating.toString()
                val user_review=dialogview.findViewById<EditText>(R.id.user_review).text.toString()
                if (rating_val.equals("0.0"))
                {
                    Toast.makeText(activity,"Please select your rating",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else {
                    var name = BasePrefs.getString(
                        ChoreConstants.PrefNames.PREF_NAME_USER,
                        ChoreConstants.PrefKeys.PREF_KEY_LOGGED_IN_USER_ID
                    ).toString()
                    val data = hashMapOf(
                        "Rating" to rating_val,
                        "review" to user_review,
                        "user_id" to name
                    )
                    FirebaseFirestore.getInstance().collection("user_ratings_&_reviews").add(data)
                        .addOnSuccessListener {

                            dialog.dismiss()
                            Toast.makeText(activity, "Thank you for rating.", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                        Toast.makeText(activity, "Sorry. ", Toast.LENGTH_SHORT).show()
                    }
                }


            }


        }

        return root



    }


}