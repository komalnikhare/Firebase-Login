package com.example.firebaselogin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.firebaselogin.R
import com.example.firebaselogin.ui.login.LoginActivity
import com.example.firebaselogin.data.ViewModelFactory

class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashViewModel =  ViewModelProviders.of(
            this, ViewModelFactory(
                this.application, this@SplashActivity
            )
        )
            .get(SplashViewModel::class.java)

        checkAuthentication()
    }

    private fun checkAuthentication() {
        splashViewModel.checkAuthentication()
        splashViewModel.authenticateLiveData.observe(this@SplashActivity, Observer {
            val verificationData = it?: return@Observer

            if (verificationData.isAuthenticated){
                startActivity(Intent(this@SplashActivity,
                    MainActivity::class.java)
                    .apply {
                        this.putExtra("Message", "Already Logged In")
                    })
            }else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        })
    }
}
