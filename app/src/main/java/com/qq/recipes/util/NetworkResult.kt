package com.qq.recipes.util

sealed class NetworkResult<T>(data: T? = null, message: String? = null) {

    class Success<T>(data: T, message: String? = null) : NetworkResult<T>(data, message)
    class Error<T>(data: T? = null, message: String? = null) : NetworkResult<T>(data, message)
    class Loading<T>() : NetworkResult<T>()

}