package com.book.chore

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class ChoreApplication : Application() {
    var firestoreInstance: FirebaseFirestore? = null
    override fun onCreate() {
        super.onCreate()
        firestoreInstance = FirebaseFirestore.getInstance()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}