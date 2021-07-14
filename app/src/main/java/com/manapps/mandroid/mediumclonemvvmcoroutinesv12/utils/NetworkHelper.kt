package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())

        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.GenericError(103, "Not Connected To Internet")
                is UnknownHostException -> ResultWrapper.GenericError(101, ErrorStatus.NO_CONNECTION)
                is SocketTimeoutException -> ResultWrapper.GenericError(102, ErrorStatus.TIMEOUT)
                is HttpException -> ResultWrapper.GenericError(throwable.code(), "HttpException")
                else -> {
                    ResultWrapper.GenericError(null, throwable.localizedMessage)
                }
            }
        }
    }
}


object NetworkHelper {

    fun isNetworkConnected(context: Context): Boolean {
        var isNetworkConnected = false
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network) ?: return false
            isNetworkConnected = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    isNetworkConnected = isConnected
                }
            }
        }
        if (!isNetworkConnected){
            Utils.showMessage(context,context.resources.getString(R.string.noNetworkConnectedError))
        }

        return isNetworkConnected
    }
}


