package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Utils
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.feedArticles.MainActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.databinding.ActivitySplashBinding
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.login.LoginActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.ViewModelProviderFactory
import java.lang.IllegalStateException
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel
    private val deleyTime: Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        setUpViewModel()
        setUpObservers()
        viewModel.checkSessionHistory()
    }


    private fun initBindings() {
        try {
            binding = ActivitySplashBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: IllegalStateException) {
        } catch (e: Exception) {
        }

    }

    private fun setUpViewModel() {
        viewModel =
                ViewModelProviders.of(
                        this,
                        ViewModelProviderFactory(SplashViewModel::class) {
                            SplashViewModel(this)
                        }
                )
                        .get(SplashViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.goToLogin.observe(this, {
            if (it) {
                Timer().schedule(timerTask {
                    Utils.moveTo(this@SplashActivity, LoginActivity::class.java)
                }, deleyTime)
            }
        })

        viewModel.goToMain.observe(this, {
            if (it) {
                Timer().schedule(timerTask {
                    Utils.moveTo(this@SplashActivity, MainActivity::class.java)
                }, deleyTime)
            }
        })
    }
}