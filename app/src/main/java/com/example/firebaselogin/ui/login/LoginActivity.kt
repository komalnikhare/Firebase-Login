package com.example.firebaselogin.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.firebaselogin.ui.MainActivity

import com.example.firebaselogin.R
import com.example.firebaselogin.Utils
import com.example.firebaselogin.data.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(this.application, this@LoginActivity)
        )
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
                password.visibility = View.GONE
                login.setText(R.string.action_sign_in)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })





        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.verificationDataChanged(
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (login.text.toString().equals(other = "Verify", ignoreCase = true)) {
                            verifyMobile()
                        } else {
                            sendVerificationCode()
                        }
                }
                false
            }

            login.setOnClickListener {
                Utils.hideKeyboard(this@LoginActivity)
                loading.visibility = View.VISIBLE

                if (login.text.toString().equals(other = "Verify", ignoreCase = true)) {
                    verifyMobile()
                } else {
                    sendVerificationCode()
                }
            }
        }
    }

    private fun sendVerificationCode() {
        loginViewModel.sendVerificationCode(username.text.toString())

        loginViewModel.verificationData.observe(this@LoginActivity, Observer {

            val verificationData = it ?: return@Observer

            loading.visibility = View.GONE
            if (verificationData.error != null) {
                showLoginFailed(verificationData.error)

            }
            if (verificationData.success != null) {
                password.visibility = View.VISIBLE
                password.setText(verificationData.success.verificationCode)
                login.setText(R.string.action_verify)
            }

        })
    }

    private fun verifyMobile() {
        loginViewModel.verifyMobile(password.text.toString())

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            //setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        //password.visibility = View.VISIBLE
        startActivity(Intent(
            this@LoginActivity,
            MainActivity::class.java
        )
            .apply {
                this.putExtra("Message", "Login Success")
            })
    }

    private fun showLoginFailed(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
