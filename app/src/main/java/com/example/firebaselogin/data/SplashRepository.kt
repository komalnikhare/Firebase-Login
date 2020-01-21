package com.example.firebaselogin.data

import androidx.lifecycle.MutableLiveData
import com.example.firebaselogin.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore



class SplashRepository {
    private val mAuth: FirebaseAuth
    private val rootRef: FirebaseFirestore

    //private val usersRef: CollectionReference
    init {
        mAuth = FirebaseAuth.getInstance()
        rootRef = FirebaseFirestore.getInstance()
        //usersRef = rootRef.collection(USERS)
    }

    fun checkAuthentication(): MutableLiveData<LoggedInUser>{
        val isUserAuthenticate = MutableLiveData<LoggedInUser>()
        val firebaseUser = mAuth.currentUser
        if (firebaseUser == null){
            val user = LoggedInUser(null, null,null, false)
            isUserAuthenticate.value = user
        }else{
            val user = LoggedInUser(firebaseUser?.uid,
                firebaseUser?.displayName,
                firebaseUser?.phoneNumber,
                true)
            isUserAuthenticate.value = user
        }

        return isUserAuthenticate
    }
}