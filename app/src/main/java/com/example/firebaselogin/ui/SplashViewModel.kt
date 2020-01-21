package com.example.firebaselogin.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firebaselogin.data.SplashRepository
import com.example.firebaselogin.data.model.LoggedInUser

class SplashViewModel(application: Application, private val splashRepository: SplashRepository) : AndroidViewModel(application) {

     lateinit var authenticateLiveData: LiveData<LoggedInUser>

    fun checkAuthentication(){
        authenticateLiveData = splashRepository.checkAuthentication()
    }


}