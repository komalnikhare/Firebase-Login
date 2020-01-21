package com.example.firebaselogin.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.example.firebaselogin.data.LoginRepository

import com.example.firebaselogin.R

class LoginViewModel(application: Application, private val loginRepository: LoginRepository) : AndroidViewModel(application) {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    lateinit var verificationData: LiveData<LoginResult>
    lateinit var loginResult: LiveData<LoginResult>


    fun sendVerificationCode(phoneNo: String) {
        verificationData =  loginRepository.sendVerificationCode(phoneNo)
    }

    fun verifyMobile(code: String){
        loginResult = loginRepository.validateMobile(code)
    }

    fun loginDataChanged(phoneNo: String) {
        if (!isUserNameValid(phoneNo)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    fun verificationDataChanged( password: String) {
         if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(phoneNo: String): Boolean {
        return phoneNo.isNotEmpty() && phoneNo.contains("+")
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }
}
