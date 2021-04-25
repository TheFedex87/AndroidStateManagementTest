package com.example.statetest

open class State<T>(
    private val content: T?
) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(
        peekAlwaysOnSuccess: Boolean = true,
        peekAlwaysOnError: Boolean = false,
        peekAlwaysOnLoading: Boolean = true
    ): T? {
        return if (hasBeenHandled || content == null) {
            null
        } else {
            if ((content is Resource.Success<*> && !peekAlwaysOnSuccess) ||
                (content is Resource.Error<*> && !peekAlwaysOnError) ||
                (content is Resource.Loading<*> && !peekAlwaysOnLoading)
            ) {
                hasBeenHandled = true
            }
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T? = content
}