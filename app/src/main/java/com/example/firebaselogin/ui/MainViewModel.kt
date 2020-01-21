package com.example.firebaselogin.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.firebaselogin.data.MainRepository

class MainViewModel(application: Application, private val mainRepository: MainRepository): AndroidViewModel(application) {

    lateinit var messages: LiveData<List<String>>
    var mediatorLiveData= MediatorLiveData<List<String>>()
    lateinit var responseLiveData: LiveData<String>

    fun getMessageCollection(){
        messages = mainRepository.getMessageCollection()
        mediatorLiveData?.addSource(messages as LiveData<List<String>>) {
            mediatorLiveData?.value = it
        }
    }

    fun getUpdatedData(){
        if (messages != null ){
            mediatorLiveData?.removeSource(messages as LiveData<List<String>>)
        }
        messages = mainRepository.getMessageCollection()
        mediatorLiveData?.addSource(messages as LiveData<List<String>>) {
            mediatorLiveData?.value = it
        }
    }

    fun saveMessage(message: String){
        responseLiveData = mainRepository.saveMessage(message)

    }

    fun signOut(){
        mainRepository.signOut()
    }
}
