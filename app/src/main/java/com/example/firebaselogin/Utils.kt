package com.example.firebaselogin

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


object Utils {

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}