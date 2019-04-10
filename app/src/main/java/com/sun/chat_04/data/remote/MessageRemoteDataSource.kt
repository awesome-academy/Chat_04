package com.sun.chat_04.data.remote

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sun.chat_04.data.model.Friend
import com.sun.chat_04.data.model.Message
import com.sun.chat_04.data.repositories.MessageDataSource
import com.sun.chat_04.ui.signup.RemoteCallback
import com.sun.chat_04.util.Constants
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.util.Date

class MessageRemoteDataSource(
    auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    friend: Friend,
    private val storage: FirebaseStorage
) :
    MessageDataSource.Remote {

    private val uid = auth.currentUser?.uid.toString()
    private val uidUserRec = friend.idUser
    private val userSendRef = "${Constants.MESSAGES}/$uid/$uidUserRec"
    private val userRecRef = "${Constants.MESSAGES}/$uidUserRec/$uid"

    override fun insertMessage(message: Message, bitmap: Bitmap?, callback: RemoteCallback<Boolean>) {
        val idMessage = database.reference.child(userSendRef)
            .child(userRecRef).push().key
        if (message.type == Constants.TEXT_MESSAGE) {
            updateMessageDetail(null, Constants.TEXT_MESSAGE, message, idMessage.toString())
        } else {
            val uriImage = Uri.parse(message.contents)
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
            val byteArray = outputStream.toByteArray()
            val pathImage = DateFormat.getDateTimeInstance().format(Date())
            uriImage?.let {
                storage.reference.child(Constants.MESSAGES)
                    .child(pathImage)
                    .putBytes(byteArray)
                    .addOnSuccessListener {
                        outputStream.close()
                        callback.onSuccessfuly(true)
                        storage.reference.child(Constants.MESSAGES)
                            .child(pathImage).downloadUrl
                            .addOnSuccessListener { uri ->
                            updateMessageDetail(uri.toString(), Constants.IMAGE_MESSAGE, message, idMessage.toString())
                        }
                            .addOnFailureListener { callback.onFailure(it) }
                    }
                    .addOnFailureListener { callback.onFailure(it) }

            }
        }
    }

    private fun updateMessageDetail(uri: String?, type: String, message: Message, idMessage: String) {
        val bodyMessage = HashMap<String, String>()
        bodyMessage[Constants.FROM] = uid
        bodyMessage[Constants.ID_MESSAGE] = idMessage
        when (type) {
            Constants.IMAGE_MESSAGE -> {
                bodyMessage[Constants.CONTENTS] = uri.toString()
            }
            Constants.TEXT_MESSAGE -> {
                bodyMessage[Constants.CONTENTS] = message.contents
            }
        }
        bodyMessage[Constants.TYPE] = type
        val detailMessage = HashMap<String, Any>()
        detailMessage["$userRecRef/$idMessage"] = bodyMessage
        detailMessage["$userSendRef/$idMessage"] = bodyMessage
        database.reference.updateChildren(detailMessage)
    }

    override fun getMessages(callback: RemoteCallback<ArrayList<Message>>) {
        val messages: ArrayList<Message> = ArrayList()
        database.reference.child(Constants.MESSAGES).child(uid).child(uidUserRec)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    callback.onFailure(error.toException())
                }

                override fun onDataChange(data: DataSnapshot) {
                    messages.clear()
                    for (dataSnapshot in data.children) {
                        val messaage = dataSnapshot.getValue(Message::class.java)
                        messaage?.let {
                            messages.add(messaage)
                            callback.onSuccessfuly(messages)
                        }
                    }
                }
            })
    }

    companion object {
        const val IMAGE_QUALITY = 10
    }
}
