package com.example.firebaselogin.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val uid: String?,
    val displayName: String?,
    val phoneNumber: String?,
    val isAuthenticated:  Boolean = false
)
