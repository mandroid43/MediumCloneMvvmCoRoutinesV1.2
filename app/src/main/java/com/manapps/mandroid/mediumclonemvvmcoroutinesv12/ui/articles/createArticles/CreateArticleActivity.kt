package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.createArticles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.APIClinet
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository.CreateArticlesRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.databinding.ActivityCreateArticleBinding
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.*

class CreateArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateArticleBinding
    private val tagList: List<String> = listOf()
    private lateinit var viewModel: CreateArticlesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        setUpToolBar()
        APIClinet.authToken = SharedPref.getSavedUserData(this, Constants.Token)
        setUpViewModel()
        setUpObservers()

        binding.createArticleBtn.setOnClickListener {
            viewModel.checkValidationsAndSendLoginRequest(
                binding.titleEt.text.toString().trim(),
                binding.descriptionEt.text.toString().trim(),
                binding.bodyEt.text.toString().trim(),
                tagList
            )
        }

        binding.titleEt.afterTextChanged {
            viewModel.onTitleChanged(binding.titleEt.text.toString().trim())
        }
        binding.descriptionEt.afterTextChanged {
            viewModel.onDescChanged(binding.descriptionEt.text.toString().trim())
        }
        binding.bodyEt.afterTextChanged {
            viewModel.onBodyChanged(binding.bodyEt.text.toString().trim())
        }
    }

    private fun initBindings() {
        binding = ActivityCreateArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolBar)
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                    ViewModelProviderFactory(CreateArticlesViewModel::class) {
                        CreateArticlesViewModel(CreateArticlesRepository(APIClinet.authApi), this)
                    }
            )
                .get(CreateArticlesViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.titleField.observe(this, {
            if (binding.titleEt.text.toString().trim() != it) binding.titleEt.setText(it)
        })
        viewModel.descField.observe(this, {
            if (binding.descriptionEt.text.toString().trim() != it) binding.descriptionEt.setText(it)
        })
        viewModel.bodyField.observe(this, {
            if (binding.bodyEt.text.toString().trim() != it) binding.bodyEt.setText(it)
        })
        viewModel.titleFieldValidation.observe(this, {
            when (it.status) {
                ResultStatus.ERROR -> {
                    binding.titleInputLayout.error = it.message
                }
                else -> {
                    binding.titleInputLayout.isErrorEnabled = false
                }
            }
        })
        viewModel.descFieldValidation.observe(this, {
            when (it.status) {
                ResultStatus.ERROR -> {
                    binding.descriptionInputLayout.error = it.message
                }
                else -> {
                    binding.descriptionInputLayout.isErrorEnabled = false
                }
            }
        })
        viewModel.bodyFieldValidation.observe(this, {
            when (it.status) {
                ResultStatus.ERROR -> {
                    binding.bodyInputLayout.error = it.message
                }
                else -> {
                    binding.bodyInputLayout.isErrorEnabled = false
                }
            }
        })
        viewModel.createArticlesResult.observe(this, {
            it?.let {
                when (it.status) {
                    ResultStatus.SUCCESS -> {
                        setProgressDialogDisable()
                        finish()
                    }
                    ResultStatus.ERROR -> {
                        setProgressDialogDisable()
                        Utils.showMessage(this, it.message)
                    }
                    ResultStatus.LOADING -> {
                        setProgressDialogEnable()
                    }
                }
            }
        })
    }


    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }


}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}