package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.viewArticles

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticleResponse
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository.ArticlesDetailsRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class ArticlesDetailsViewModel(val repository: ArticlesDetailsRepository, private val context: Context) : ViewModel() {
    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable


    private val _articlesDetailsLiveData = MutableLiveData<Resource<ArticleResponse>>()
    val articlesDetailsLiveData: LiveData<Resource<ArticleResponse>>
        get() = _articlesDetailsLiveData


    fun checkSlugIdAndLoadArticleDetails(slugId: String?) {
        if (!slugId.isNullOrEmpty()) {
            if (NetworkHelper.isNetworkConnected(context)) {
                sendGetFeedArticlesRequest(slugId)
            } else {
                _isNetworkAvailable.postValue(false)
            }
        } else {
            Utils.showMessage(context, context.resources.getString(R.string.slugIdNotFoundMessage))
        }
    }

    fun sendGetFeedArticlesRequest(slugId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _articlesDetailsLiveData.postValue(Resource.loading(null))
            when (val response = repository.getArticleBySlug(slugId)) {
                is ResultWrapper.GenericError -> setArticlesDetailsError(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) response.value.body()?.let {
                        _articlesDetailsLiveData.postValue(Resource.success(it))
                    }
                    else {
                        when (response.value.code()) {
                            400 -> {
                                setArticlesDetailsError(
                                        handleError(
                                                response.value.errorBody()?.string()
                                        )
                                )
                            }
                            else -> {
                                setArticlesDetailsError(response.value.raw().message)
                            }
                        }
                    }
                }


            }
        }
    }

    private fun setArticlesDetailsError(message: String) {
        _articlesDetailsLiveData.postValue(Resource.error(null, message))
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