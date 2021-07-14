package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.signup

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.SignupData
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.request.SignupRequest
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.UserResponse
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository.CreateAccountRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class CreateAccountViewModel(private val repository: CreateAccountRepository, private val context: Context):ViewModel() {
    private val _emailField = MutableLiveData<String>()
    val emailField: LiveData<String> = _emailField

    private val _passwordField = MutableLiveData<String>()
    val passwordField: LiveData<String> = _passwordField

    private val _userNameField = MutableLiveData<String>()
    val userNameField: LiveData<String> = _userNameField

    private val _emailFieldValidation = MutableLiveData<Resource<Int>>()
    val emailFieldValidation: LiveData<Resource<Int>> = _emailFieldValidation

    private val _passwordFieldValidation = MutableLiveData<Resource<String>>()
    val passwordFieldValidation: LiveData<Resource<String>> = _passwordFieldValidation

    private val _userNamFieldValidation = MutableLiveData<Resource<String>>()
    val userNamFieldValidation: LiveData<Resource<String>> = _userNamFieldValidation

    private val _signupResultLiveData = MutableLiveData<Resource<UserResponse>>()
    val signupResultLiveData: LiveData<Resource<UserResponse>>
        get() = _signupResultLiveData

    fun onEmailChanged(email: String) {
        _emailField.value = email
        isEmailValid(email)
    }

    fun onPasswordChanged(password: String) {
        _passwordField.value = password
        isPasswordValid(password)
    }

    fun onUserNameChanged(userName: String) {
        _userNameField.value = userName
        isUserNameValid(userName)
    }

    private fun isEmailValid(email: String): Boolean {
        var isValidEmail = false
        if (email.isEmpty()) {
            _emailFieldValidation.value = Resource.error(null, context.resources.getString(R.string.emptyEmail))
        } else {
            isValidEmail =
                    (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            if (!isValidEmail) {
                _emailFieldValidation.value = Resource.error(null, context.resources.getString(R.string.invalidEmail))
            } else {
                _emailFieldValidation.value = Resource.success(null)
            }
        }
        return isValidEmail
    }

    private fun isPasswordValid(password: String): Boolean {
        var isValidPassword = false
        if (password.isEmpty()) {
            _passwordFieldValidation.value = Resource.error(null, context.resources.getString(R.string.emptyPassword))
        } else {
            isValidPassword = password.length > 7
            if (isValidPassword) {
                _passwordFieldValidation.value = Resource.success(null)
            } else {
                _passwordFieldValidation.value = Resource.error(null, context.resources.getString(R.string.invalidPassword))
            }
        }
        return isValidPassword
    }

    private fun isUserNameValid(userName: String): Boolean {
        var isValidUserName = false
        if (userName.isEmpty()) {
            _userNamFieldValidation.value = Resource.error(null, context.resources.getString(R.string.emptyUserName))
        } else {
            _userNamFieldValidation.value = Resource.success(null)
            isValidUserName = true
        }
        return isValidUserName
    }


    fun checkValidationsAndSendCreateAccountRequest(userName: String, email: String, password: String) {
        if (isUserNameValid(userName) && isEmailValid(email) && isPasswordValid(password) && NetworkHelper.isNetworkConnected(context)) {
            sendCreateAccountRequest(SignupRequest(SignupData(email, password, userName)))
        }
    }

    private fun sendCreateAccountRequest(signupRequest: SignupRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            _signupResultLiveData.postValue(Resource.loading(null))
            when (val response = repository.sendCreateAccountRequest(signupRequest)) {
                is ResultWrapper.GenericError -> setSignupError(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) response.value.body()?.let {
                        _signupResultLiveData.postValue(Resource.success(it))
                    }
                    else {
                        when (response.value.code()) {
                            400 -> {
                                setSignupError(
                                        handleError(
                                                response.value.errorBody()?.string()
                                        )
                                )
                            }
                            else -> {
                                setSignupError(response.value.raw().message)
                            }
                        }
                    }
                }


            }
        }
    }


    private fun setSignupError(message: String) {
        // _signupResultLiveData.postValue(Resource.error(null, message))
        _signupResultLiveData.postValue(Resource.error(null, "Invalid Email or UserName"))
    }

    private fun handleError(string: String?): String {
        val gson = GsonBuilder().create()
        val mError: ErrorClass?
        var message = ""
        try {
            mError = gson.fromJson(string, ErrorClass::class.java)
            if (mError != null) {
                if (mError.errors.size > 0) {
                    message = mError.errors[0]
                }
            } else {
                message = ErrorStatus.GENERAL_ERROR
            }
        } catch (a: IllegalStateException) {
            Log.e("a", a.toString())
            message = ""
        }
        return message
    }
}