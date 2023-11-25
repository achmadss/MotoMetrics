package com.achmadss.data

sealed class DataState<out T> {
    data object Loading : DataState<Nothing>()
    data class Success<out Result>(val data: Result) : DataState<Result>()
    data class Error(val error: Throwable) : DataState<Nothing>()
}