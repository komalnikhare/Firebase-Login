package com.example.firebaselogin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.firebaselogin.data.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.view.MenuItem
import com.example.firebaselogin.R
import com.example.firebaselogin.Utils


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private var messageList = listOf<String>()
    var listAdapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val message = intent.getStringExtra("Message")

        tvMessage.text = message

        mainViewModel = ViewModelProviders.of(
            this, ViewModelFactory(
                this.application, this@MainActivity
            )
        ).get(MainViewModel::class.java)

        listAdapter = ListAdapter(this@MainActivity, messageList)
        recyclerView.apply {
            adapter = listAdapter
        }

        btnSave.setOnClickListener {
            Utils.hideKeyboard(this@MainActivity)
            sendData(text.text.toString())

        }

        loadMessages()
    }

    private fun loadMessages(){
        mainViewModel.getMessageCollection()
        mainViewModel.mediatorLiveData.observe(this@MainActivity, Observer {
            val messages = it?: return@Observer

            if (messages != null){
                listAdapter?.setList(messages as ArrayList<String>)
            }
        })
    }

    private fun sendData(message: String){
        text.setText("")
        mainViewModel.saveMessage(message)
        mainViewModel.responseLiveData.observe(this@MainActivity, Observer {
            val response = it?: return@Observer

            if (response!= null && response.equals("success",true)){
                mainViewModel.getUpdatedData()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logOut){

            mainViewModel.signOut()
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            finish()

            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

