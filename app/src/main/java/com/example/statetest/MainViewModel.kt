package com.example.statetest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
//    private val _data = MutableLiveData<State<MainViewModelState>>()
//    val data: LiveData<State<MainViewModelState>> get() = _data

    private val repository = Repository.getInstance()

    val stateManager = StateManager<MainViewModelState>()

    init {
        Log.d("StateTest", "Init view model")
        viewModelScope.launch {
            repository.dataFetchState.collect {
                when (it) {
                    is Resource.Success -> {
                        //_data.value = State(MainViewModelState.Success(it.data!!))
                        stateManager.setState(MainViewModelState.Success(it.data!!))
                    }
                    is Resource.Error -> {
                        //_data.value = State(MainViewModelState.Error(null, it.throwable!!.message!!))
                        stateManager.setState(
                            MainViewModelState.Error(
                                null,
                                it.throwable!!.message!!
                            )
                        )
                    }
                    is Resource.Loading -> {
                        //_data.value = State(MainViewModelState.Loading())
                        stateManager.setState(MainViewModelState.Loading())
                    }
                    is Resource.Empty -> {

                    }
                }
            }
        }
    }

    fun emitValue() {
        //_data.value = State(MainViewModelState.State2())
        viewModelScope.launch {
            stateManager.setState(MainViewModelState.State2())
        }
    }

    fun getData(simulateError: Boolean) {
        viewModelScope.launch {
            repository.getData(simulateError)
        }
    }
}

//sealed class MainViewModelState(
//    val data: Int? = null,
//) : Event {
//    class Success(data: Int, override val emitAllTimes: Boolean = true) : MainViewModelState(data)
//    class Loading(data: Int? = null, override val emitAllTimes: Boolean = true) : MainViewModelState(data)
//    class Error(data: Int? = null, val message: String, override val emitAllTimes: Boolean = false) : MainViewModelState(data)
//    data class State1(override val emitAllTimes: Boolean = true) : MainViewModelState(null)
//    data class State2(override val emitAllTimes: Boolean = false) : MainViewModelState(null)
//}

sealed class MainViewModelState(
    val data: Int? = null,
    override val isEvent: Boolean = false,
    override val resetStateOnEvent: Boolean = false
) : StateManager.EventManager {
    class Success(data: Int, isEvent: Boolean = false, resetStateOnEvent: Boolean = false) :
        MainViewModelState(data, isEvent, resetStateOnEvent)

    class Loading(data: Int? = null, isEvent: Boolean = false, resetStateOnEvent: Boolean = false) :
        MainViewModelState(data, isEvent, resetStateOnEvent)

    class Error(
        data: Int? = null,
        val message: String,
        isEvent: Boolean = true,
        resetStateOnEvent: Boolean = true
    ) : MainViewModelState(data, isEvent, resetStateOnEvent)

    class State1(isEvent: Boolean = false, resetStateOnEvent: Boolean = false) :
        MainViewModelState(null, isEvent, resetStateOnEvent)

    class State2(isEvent: Boolean = true, resetStateOnEvent: Boolean = false) :
        MainViewModelState(null, isEvent, resetStateOnEvent)
}