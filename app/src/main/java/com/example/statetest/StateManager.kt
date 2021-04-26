package com.example.statetest

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class StateManager<T: StateManager.EventManager> {
    private val _event = MutableSharedFlow<T>()
    val event: LiveData<T> get() = _event.asLiveData()

    private val _state = MutableStateFlow<T?>(null)
    val state: LiveData<T?> get() = _state.asLiveData()

    

    suspend fun setState(state: T) {
        if(state.isEvent) {
            if(state.resetStateOnEvent) {
                _state.value = null
            }
            _event.emit(state)
        } else {
            _state.value = state
        }
    }

    interface EventManager {
        val isEvent: Boolean
        val resetStateOnEvent: Boolean
    }
}