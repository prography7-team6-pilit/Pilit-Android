package com.prography.pilit.data.datasource.remote

sealed class Resource<out T>{
    class Success<out T>(val data: T) : Resource<T>()
    class Error<out T>(val errorMessage: String? = null) : Resource<T>()
}