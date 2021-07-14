package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.createArticles

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.CreateArticle
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.request.CreateArticleRequest
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticleResponse
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository.CreateArticlesRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class CreateArticlesViewModel(private val repository: CreateArticlesRepository, private val context: Context) :
        ViewModel() {
    private val _titleField = MutableLiveData<String>()
    val titleField: LiveData<String> = _titleField

    private val _descField = MutableLiveData<String>()
    val descField: LiveData<String> = _descField

    private val _bodyField = MutableLiveData<String>()
    val bodyField: LiveData<String> = _bodyField

    private val _titleFieldValidation = MutableLiveData<Resource<Int>>()
    val titleFieldValidation: LiveData<Resource<Int>> = _titleFieldValidation

    private val _descFieldValidation = MutableLiveData<Resource<Int>>()
    val descFieldValidation: LiveData<Resource<Int>> = _descFieldValidation

    private val _bodyFieldValidation = MutableLiveData<Resource<Int>>()
    val bodyFieldValidation: LiveData<Resource<Int>> = _bodyFieldValidation


    private val _createArticlesResult = MutableLiveData<Resource<ArticleResponse>>()
    val createArticlesResult: LiveData<Resource<ArticleResponse>>
        get() = _createArticlesResult


    fun onTitleChanged(title: String) {
        _titleField.value = title
        isTitleValid(title)
    }

    fun onDescChanged(desc: String) {
        _descField.value = desc
        isDescValid(desc)
    }

    fun onBodyChanged(body: String) {
        _bodyField.value = body
        isBodyValid(body)
    }

    fun checkValidationsAndSendLoginRequest(
            title: String,
            desc: String,
            body: String,
            tagList: List<String>
    ) {
        if (isTitleValid(title) && isDescValid(desc) && isBodyValid(body)&& NetworkHelper.isNetworkConnected(context)) {
            sendCreateArticleRequest(
                    CreateArticleRequest(
                            CreateArticle(
                                    title,
                                    desc,
                                    body,
                                    tagList
                            )
                    )
            )
        }
    }

    private fun isBodyValid(body: String): Boolean {
        if (body.isEmpty()) {
            _bodyFieldValidation.value =
                    Resource.error(null, context.resources.getString(R.string.emptyArticleBody))
            return false
        } else {
            _bodyFieldValidation.value = Resource.success(null)
        }
        return true
    }

    private fun isDescValid(desc: String): Boolean {
        if (desc.isEmpty()) {
            _descFieldValidation.value = Resource.error(
                    null, context.resources.getString(
                    R.string.emptyArticleDescription
            )
            )
            return false
        } else {
            _descFieldValidation.value = Resource.success(null)
        }
        return true

    }

    private fun isTitleValid(title: String): Boolean {
        if (title.isEmpty()) {
            _titleFieldValidation.value =
                    Resource.error(null, context.resources.getString(R.string.emptyArticleTitle))
            return false
        } else {
            _titleFieldValidation.value = Resource.success(null)
        }
        return true
    }


    fun sendCreateArticleRequest(createArticleRequest: CreateArticleRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            _createArticlesResult.postValue(Resource.loading(null))
            when (val response = repository.sendCreateArticleRequest(createArticleRequest)) {
                is ResultWrapper.GenericError -> setCreateArticlesError(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) response.value.body()?.let {
                        _createArticlesResult.postValue(Resource.success(it))
                    }
                    else {
                        when (response.value.code()) {
                            400 -> {
                                setCreateArticlesError(
                                        handleError(
                                                response.value.errorBody()?.string()
                                        )
                                )
                            }
                            else -> {
                                setCreateArticlesError(response.value.raw().message)
                            }
                        }
                    }
                }


            }
        }
    }

    private fun setCreateArticlesError(message: String) {
        _createArticlesResult.postValue(Resource.error(null, message))
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