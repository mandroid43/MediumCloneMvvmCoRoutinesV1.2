package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.feedArticles

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.gson.GsonBuilder
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse
import com.manapps.mandroid.mediumclonemvckotlin.data.repository.FeedArticlesRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class FeedArticlesViewModel(val repository: FeedArticlesRepository, val context: Context) :
    ViewModel() {

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable

    private val _feedArticlesLiveData = MutableLiveData<Resource<ArticlesResponse>>()
    val feedArticlesLiveData: LiveData<Resource<ArticlesResponse>>
        get() = _feedArticlesLiveData

    /** ROOM DATABASE */
    val feedArticles: LiveData<List<Article>> = repository.getArticles().asLiveData()


    fun checkNetworkAndLoadFeedArticles() {
        if (NetworkHelper.isNetworkConnected(context)) {
            sendGetFeedArticlesRequest()
        } else {
            _isNetworkAvailable.postValue(false)

        }
    }

    fun sendGetFeedArticlesRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            _feedArticlesLiveData.postValue(Resource.loading(null))
            when (val response = repository.getAuthorsArticles()) {
                is ResultWrapper.GenericError -> setFeedArticlesError(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) response.value.body()?.let {
                        repository.deleteAllArticles()
                        repository.insertArticles(it.articles)
                        _feedArticlesLiveData.postValue(Resource.success(it))
                    }
                    else {
                        when (response.value.code()) {
                            400 -> {
                                setFeedArticlesError(
                                    handleError(
                                        response.value.errorBody()?.string()
                                    )
                                )
                            }
                            else -> {
                                setFeedArticlesError(response.value.raw().message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setFeedArticlesError(message: String) {
        _feedArticlesLiveData.postValue(Resource.error(null, message))
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

    fun logOutSession() {
        repository.logoutSession(context)
    }

    fun gotoProfile() {
        repository.gotoProfile(context)
    }

}