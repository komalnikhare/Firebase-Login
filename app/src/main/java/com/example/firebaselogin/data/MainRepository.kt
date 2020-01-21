package com.example.firebaselogin.data

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainRepository (private val activity: Activity){

    private val mAuth: FirebaseAuth
    private val mFirebaseFirestore: FirebaseFirestore

    init {
        mAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()
    }

    fun getMessageCollection(): MutableLiveData<List<String>> {
        val messages = MutableLiveData<List<String>>()
        mFirebaseFirestore.collection("messages").get()
            .addOnSuccessListener { documentSnapshots ->
                if (documentSnapshots.isEmpty) {
                    Log.d("Message", "onSuccess: LIST EMPTY");
                    messages.value = null
                    return@addOnSuccessListener
                } else {
                    val documents = documentSnapshots.documents
                    val data = arrayListOf<String>()
                    for (value in documents) {
                        data.add(value.data?.get("text").toString())
                    }
                    Log.d("Message1", "onSuccess: ${data}");
                    messages.value = data
                }

            }
            .addOnFailureListener {
                Log.d("Message", "failure : ${it.message}");
                messages.value = null
            }
        return messages
    }

    fun saveMessage(message: String): MutableLiveData<String>{
        val newMessage = MutableLiveData<String>()
        val item = HashMap<String, Any>()
        item["text"] = message
        mFirebaseFirestore.collection("messages")
            .document()
            .set(item)
            .addOnSuccessListener {
                Log.d("Message", "Success");
                newMessage.value = "success"
            }
            .addOnFailureListener {
                Log.e("Message", "failure : ${it.message}");
                newMessage.value = "failed"
            }

        return newMessage

    }

    fun signOut(){
        mAuth.signOut()
    }

}