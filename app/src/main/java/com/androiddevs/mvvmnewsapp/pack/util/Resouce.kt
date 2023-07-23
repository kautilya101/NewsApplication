package com.androiddevs.mvvmnewsapp.pack.util


sealed class Resouce<T>(
    val data :T? = null,
    val message: String? = null

) {
    class Success<T>(data: T) : Resouce<T>(data)
    class Error<T>(message: String?,data: T? = null) : Resouce<T>(data,message)
    class Loading<T> : Resouce<T>()
}