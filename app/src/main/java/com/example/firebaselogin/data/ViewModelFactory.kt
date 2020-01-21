package com.example.firebaselogin.data

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebaselogin.ui.MainViewModel
import com.example.firebaselogin.ui.SplashViewModel
import com.example.firebaselogin.ui.login.LoginViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ViewModelFactory(private val application: Application, private val activity: Activity) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                application,
                loginRepository = LoginRepository(activity)
            ) as T
        }else if (modelClass.isAssignableFrom(SplashViewModel::class.java)){
            return SplashViewModel(
                application,
                splashRepository = SplashRepository()
            ) as T
        }else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(
                application,
                MainRepository(activity)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
