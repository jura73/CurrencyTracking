package com.example.currencytracking.data.model

class Event<T>(private val content: T) {
    var isHandled = false
        private set
    val contentIfNotHandled: T?
        get() = if (isHandled) {
            null
        } else {
            isHandled = true
            content
        }
}