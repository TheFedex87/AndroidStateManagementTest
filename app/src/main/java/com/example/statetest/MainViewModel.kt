package com.example.statetest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<State<Resource<Int>>>()
    val data: LiveData<State<Resource<Int>>> get() = _data

    private val repository = Repository.getInstance()

    init {
        Log.d("StateTest", "Init view model")
        viewModelScope.launch {
            repository.dataFetchState.collect {
                _data.value = State(it)
            }
        }
    }

    fun getData(simulateError: Boolean) {
        viewModelScope.launch {
            repository.getData(simulateError)
        }
    }
}