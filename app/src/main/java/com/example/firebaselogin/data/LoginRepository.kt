package com.example.firebaselogin.data

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.firebaselogin.data.model.LoggedInUser
import com.example.firebaselogin.ui.login.LoggedInUserView
import com.example.firebaselogin.ui.login.LoginResult
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.lang.Appendable
import java.util.concurrent.TimeUnit
import kotlin.math.log

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(private val activity: Activity) {

    private val mAuth: FirebaseAuth
    private var mVerificationId: String? = null

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
        mAuth = FirebaseAuth.getInstance()
    }

    fun logout() {
        user = null

    }

    fun sendVerificationCode(phoneNo: String): MutableLiveData<LoginResult> {
        val verificationData = MutableLiveData<LoginResult>()

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNo,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("Login",p0.smsCode )
                    val code = p0.smsCode
                    //verificationData.value = LoginResult(success = LoggedInUserView("", code!!))
                }
                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.e("Login",p0.message )
                    verificationData.value = LoginResult(error = p0.message)
                }

                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    Log.d("Login","mVerificationId : $s" )
                    mVerificationId = s;
                    verificationData.value = LoginResult(success = LoggedInUserView("", ""))
                }
            }
        )

        return verificationData
    }

    fun validateMobile(code: String): MutableLiveData<LoginResult>{
        val loginResult = MutableLiveData<LoginResult>()

        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful){
                    Log.d("Login", "Success")
                    val user = task.result?.user
                    val loggedInUser = LoggedInUser(user?.uid, user?.displayName, user?.phoneNumber, true)
                    setLoggedInUser(loggedInUser)
                    loginResult.value = LoginResult(success = LoggedInUserView("",""))

                }else{
                    loginResult.value = LoginResult(error = com.example.firebaselogin.R.string.login_failed.toString())
                }
            }


        return loginResult
    }


    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
