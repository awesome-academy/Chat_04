package com.sun.chat_04.data.model

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UsersDAO {
    private var mFirebaseDatabase: FirebaseDatabase
    private var mDatabaseRef: DatabaseReference

    init {
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabaseRef = mFirebaseDatabase.reference
    }

    fun saveUser(user: Users, onSuccessListener: OnSuccessListener<Void>, onFailureListener: OnFailureListener) {
        mDatabaseRef.child(USERS).child(user.idUser)
            .setValue(user)
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }

    companion object {
        const val USERS = "Users"
    }
}
