package com.example.statetest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.*

class StateManager<T : StateManager.State> constructor(
    initialStateValue: T? = null
) {
    private val _event = MutableSharedFlow<EventHandler<T>>()
    val event: LiveData<EventHandler<T>> get() = _event.asLiveData()

    private val _state = MutableStateFlow(initialStateValue)
    val state: LiveData<T?> get() = _state.asLiveData()

    val singleStateEvent = MediatorLiveData<SingleStateEvent<T>>()

    init {
        singleStateEvent.addSource(event) {
            singleStateEvent.value = SingleStateEvent(singleStateEvent.value?.state, it)
        }
        singleStateEvent.addSource(state) {
            singleStateEvent.value = SingleStateEvent(it, singleStateEvent.value?.event)
        }
    }

//    val singleStateEvent = combine(
//        _state,
//        _event
//    ) { state, event ->
//        Pair(state, event)
//    }.flatMapLatest { (state, event) ->
//        flow {
//            emit(
//                SingleStateEvent(
//                    state,
//                    event
//                )
//            )
//        }
//    }.asLiveData()

    suspend fun setState(
        state: T,
        stateValueIfEvent: T? = null
    ) {
        if (state.isEvent) {
            if (state.resetStateOnEvent) {
                _state.value = stateValueIfEvent
            }
            _event.emit(EventHandler(state))
        } else {
            _state.value = state
        }
    }

    interface State {
        val isEvent: Boolean
        val resetStateOnEvent: Boolean
    }

    open class EventHandler<T>(
        private val content: T?
    ) {

        var hasBeenHandled = false
            private set // Allow external read but not write

        /**
         * Returns the content and prevents its use again.
         */
        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }

        /**
         * Returns the content, even if it's already been handled.
         */
        fun peekContent(): T? = content
    }

    data class SingleStateEvent<T>(
        val state: T?,
        val event: EventHandler<T>?
    )
}