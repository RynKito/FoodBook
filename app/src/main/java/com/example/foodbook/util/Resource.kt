package com.example.foodbook.util

/** 3 trạng thái UI: Loading, Success, Error */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
