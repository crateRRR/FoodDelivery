package com.ivkorshak.el_diaries.util

sealed class ScreenState<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : ScreenState<T>(data)

    class Loading<T>(data: T? = null) : ScreenState<T>(data)

    class Error<T>(message: String = "Error!", data: T? = null) :
        ScreenState<T>(data = data, message = message)
}