package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.splash

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Constants
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.SharedPref


class SplashViewModel(val context: Context) :ViewModel() {

    private val _goToLogin = MutableLiveData<Boolean>()
    val goToLogin: LiveData<Boolean> = _goToLogin

    private val _goToMain = MutableLiveData<Boolean>()
    val goToMain: LiveData<Boolean> = _goToMain


     fun checkSessionHistory() {
        val email: String? = SharedPref.getSavedUserData(context, Constants.Email)
        if (email.isNullOrEmpty()) {
           _goToLogin.postValue(true)
        }
        else{
            _goToMain.postValue(true)
        }
    }

}