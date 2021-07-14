package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String) : ResultWrapper<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$value]"
            is GenericError -> "Error[exception=$error]"
        }
    }

}