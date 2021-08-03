package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.ResultStatus
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Constants
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.SharedPref
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Utils
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.APIClinet
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository.LoginRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.feedArticles.MainActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.databinding.ActivityLoginBinding
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.createArticles.afterTextChanged
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.signup.CreateAccountActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.ViewModelProviderFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initViews()
        setUpViewModel()
        setUpObservers()

        binding.emailEt.setText("ttest@gmail.com")
        binding.passwordEt.setText("12345678")
        binding.loginBtn.setOnClickListener {
            viewModel.checkValidationsAndSendLoginRequest(
                binding.emailEt.text.toString().trim(),
                binding.passwordEt.text.toString().trim()
            )
        }
        binding.createAccountTv.setOnClickListener {
            Utils.moveTo(this, CreateAccountActivity::class.java)
        }
    }

    private fun initViews() {
        binding.loginBtn.setText(resources.getString(R.string.loginButtonText))
        binding.emailEt.afterTextChanged {
            viewModel.onEmailChanged(
                binding.emailEt.text.toString().trim()
            )
        }
        binding.passwordEt.afterTextChanged {
            viewModel.onPasswordChanged(
                binding.passwordEt.text.toString().trim()
            )
        }
        binding.passwordEt.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.checkValidationsAndSendLoginRequest(
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
                ViewModelProviderFactory(LoginViewModel::class) {
                    LoginViewModel(LoginRepository(APIClinet.authApi), this)
                }
            ).get(LoginViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.emailField.observe(this, {
            if (binding.emailEt.text.toString().trim() != it) binding.emailEt.setText(it)
        })
        viewModel.passwordField.observe(this, {
            if (binding.passwordEt.text.toString().trim() != it) binding.passwordEt.setText(it)
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
        viewModel.loginResultLiveData.observe(this, {
            it?.let {
                when (it.status) {
                    ResultStatus.SUCCESS -> {
                        setProgressDialogDisable()
                        APIClinet.authToken = it.data?.user?.token
                        SharedPref.saveUserData(
                            this@LoginActivity,
                            Constants.Email,
                            it.data?.user?.email
                        )
                        SharedPref.saveUserData(
                            this@LoginActivity,
                            Constants.Token,
                            it.data?.user?.token
                        )
                        Utils.moveTo(this@LoginActivity, MainActivity::class.java)
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
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: IllegalStateException) {
        } catch (e: Exception) {
        }

    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}