package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Utils
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R

object NetworkHelper {
    fun isNetworkConnected(context: Context?): Boolean {
        var isNetworkConnected = false
        if (context != null) {
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
        }
        return isNetworkConnected
    }

    fun noNetworkMessage(context: Context?) {
        context?.let {
            Utils.showMessage(it, it.resources.getString(R.string.noNetworkConnectedError))
        }

    }
}