package com.sun.chat_04.data.remote

import android.os.AsyncTask
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sun.chat_04.data.model.User
import com.sun.chat_04.ui.signup.RemoteCallback

class UserAsyncTask(val email: String, val pass: String, val iSavePresenter: RemoteCallback) :
    AsyncTask<User, Void, Boolean>(), OnSuccessListener<Void>,
    OnFailureListener {

    override fun doInBackground(vararg params: User): Boolean {
        val user = params[0]
        saveUser(user, email, pass, this, this)
        return true
    }

    override fun onSuccess(success: Void?) {
        iSavePresenter.isSaved(true)
    }

    override fun onFailure(exception: Exception) {
        iSavePresenter.isSaved(false)
    }

    fun saveUser(
        user: User, email: String, pass: String,
        onSuccessListener: OnSuccessListener<Void>,
        onFailureListener: OnFailureListener
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            user.idUser = FirebaseAuth.getInstance().currentUser!!.uid
            if (it.isSuccessful) {
                FirebaseDatabase.getInstance().reference.child(USERS)
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(user)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener)
            } else {
                iSavePresenter.isSaved(false)
            }
        }
    }

    companion object {
        private const val USERS = "Users"
    }
}
