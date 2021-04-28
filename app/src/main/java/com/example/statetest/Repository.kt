package com.example.statetest

import androidx.lifecycle.LiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class Repository private constructor() {
    companion object {
        private var instance: Repository? = null
        fun getInstance(): Repository {
            return if(instance != null) {
                instance!!
            } else {
                instance = Repository()
                instance!!
            }
        }
    }

    private val _dataFetchState = MutableStateFlow<Resource<Int>>(Resource.Empty())
    val dataFetchState: StateFlow<Resource<Int>> get() = _dataFetchState

    suspend fun getData(simulateError: Boolean = false)  {
        _dataFetchState.value = Resource.Loading(4)
        delay(4000)
        if(!simulateError)
            _dataFetchState.value = Resource.Success(5)
        else
            _dataFetchState.value = Resource.Error(Exception("TEST ERROR"), 5)
    }
}