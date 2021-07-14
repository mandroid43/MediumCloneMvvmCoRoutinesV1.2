package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils

data class Resource<out T>(val status: ResultStatus, val data: T?, val message: String?=null) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(ResultStatus.SUCCESS, data)
        fun <T> error(data: T?, message: String?) = Resource(ResultStatus.ERROR, data, message)
        fun <T> loading(data: T?) = Resource(ResultStatus.LOADING, data, null)
    }

}