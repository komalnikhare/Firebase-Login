package com.example.firebaselogin.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    var verificationCode: String
)
