package com.book.chore.data.ChoreServices

import com.book.chore.utils.ChoreConstants
import com.google.firebase.firestore.FirebaseFirestore

class ServiceManager {

    //Fetch all available services from firebase
    fun fetchAvailableServices(
        availableServices: (MutableList<ChoreService>) -> Unit
    ) {
        val choreServices: MutableList<ChoreService> = ArrayList()
        FirebaseFirestore.getInstance().collection(ChoreConstants.Collections.CHORE_SERVICES)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    if (!it.isEmpty) {
                        for (doc in it) {
                            choreServices.add(doc.toObject(ChoreService::class.java))
                        }
                        availableServices(choreServices)
                    } else {
                    }
                }
            }
    }

    //Fetch city specific available services from firebase
    fun fetchAvailableServicesWithCity(
        city: String,
        availableServices: (MutableList<ChoreService>) -> Unit
    ) {
        val choreServices: MutableList<ChoreService> = ArrayList()
        FirebaseFirestore.getInstance().collection(ChoreConstants.Collections.CHORE_SERVICES)
            .whereEqualTo("city", city)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                querySnapshot?.let {
                    if (!it.isEmpty) {
                        for (doc in it) {
                            choreServices.add(doc.toObject(ChoreService::class.java))
                        }
                        availableServices(choreServices)
                    } else {
                    }
                }
            }
    }
}