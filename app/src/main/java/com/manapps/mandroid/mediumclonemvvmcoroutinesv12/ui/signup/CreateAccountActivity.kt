package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProviders
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.ResultStatus
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Constants
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.SharedPref
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Utils
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.APIClinet
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository.CreateAccountRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.feedArticles.MainActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.databinding.ActivityCreateAccountBinding
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.createArticles.afterTextChanged
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.ViewModelProviderFactory


class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var viewModel: CreateAccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initViews()
        setUpViewModel()
        setUpObservers()
        binding.createAccountBtn.setOnClickListener {
            viewModel.checkValidationsAndSendCreateAccountRequest(
                    binding.userNameEt.text.toString().trim(),
                    binding.emailEt.text.toString().trim(),
                    binding.passwordEt.text.toString().trim()
            )
        }
    }

    private fun initViews() {
        binding.createAccountBtn.setText(resources.getString(R.string.createAccountBtnText))
        binding.userNameEt.afterTextChanged {
            viewModel.onUserNameChanged(binding.userNameEt.text.toString().trim())
        }
        binding.emailEt.afterTextChanged {
            viewModel.onEmailChanged(binding.emailEt.text.toString().trim())
        }

        binding.passwordEt.afterTextChanged {
            viewModel.onPasswordChanged(binding.passwordEt.text.toString().trim())
        }
        binding.passwordEt.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.checkValidationsAndSendCreateAccountRequest(
                        binding.userNameEt.text.toString().trim(),
                        binding.emailEt.text.toString().trim(),
                        binding.passwordEt.text.toString().trim()
                )
            }
            false
        }
    }

    private fun setUpViewModel() {
        viewModel =
                ViewModelProviders.of(
                        this,
                        ViewModelProviderFactory(CreateAccountViewModel::class) {
                            CreateAccountViewModel(CreateAccountRepository(APIClinet.authApi), this)
                        }
                )
                        .get(CreateAccountViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.userNameField.observe(this, {
            if (binding.userNameEt.text.toString().trim() != it) binding.userNameEt.setText(it)
        })

        viewModel.emailField.observe(this, {
            if (binding.emailEt.text.toString().trim() != it) binding.emailEt.setText(it)
        })
        viewModel.passwordField.observe(this, {
            if (binding.passwordEt.text.toString().trim() != it) binding.passwordEt.setText(it)
        })

        viewModel.userNamFieldValidation.observe(this, {
            when (it.status) {
                ResultStatus.ERROR -> {
                    binding.userNameInputLayout.error = it.message
                }
                else -> {
                    binding.userNameInputLayout.isErrorEnabled = false
                }
            }
        })

        viewModel.emailFieldValidation.observe(this, {
            when (it.status) {
                ResultStatus.ERROR -> {
                    binding.emailInputLayout.error = it.message
                }
                else -> {
                    binding.emailInputLayout.isErrorEnabled = false
                }
            }
        })

        viewModel.passwordFieldValidation.observe(this, {
            when (it.status) {
                ResultStatus.ERROR -> {
                    binding.passwordInputLayout.error = it.message
                }
                else -> {
                    binding.passwordInputLayout.isErrorEnabled = false
                }
            }
        })

        viewModel.signupResultLiveData.observe(this, {
            it?.let {
                when (it.status) {
                    ResultStatus.SUCCESS -> {
                        setProgressDialogDisable()
                        APIClinet.authToken = it.data?.user?.token
                        SharedPref.saveUserData(
                                this@CreateAccountActivity,
                                Constants.Email,
                                it.data?.user?.email
                        )
                        SharedPref.saveUserData(
                                this@CreateAccountActivity,
                                Constants.Token,
                                it.data?.user?.token
                        )
                        Utils.moveTo(this@CreateAccountActivity, MainActivity::class.java)
                    }
                    ResultStatus.ERROR -> {
                        setProgressDialogDisable()
                        Utils.showMessage(this, it.message!!)
                    }
                    ResultStatus.LOADING -> {
                        setProgressDialogEnable()
                    }
                }
            }
        })

    }


    private fun initBindings() {
        try {
            binding = ActivityCreateAccountBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }
        catch (e: IllegalStateException) {
            Utils.showLogMessage(e.message.toString())
        } catch (e: Exception) {
            Utils.showLogMessage(e.message.toString())
        }
    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }

}