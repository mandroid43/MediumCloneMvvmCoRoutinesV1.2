package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.viewArticles

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.manapps.mandroid.mediumclonemvckotlin.extensions.loadImage
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.APIClinet
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.ArticlesDatabase
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository.ArticlesDetailsRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.databinding.ActivityArticlesDetailsBinding
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.*

class ArticlesDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticlesDetailsBinding
    private var slugId: String? = null
    private lateinit var viewModel: ArticlesDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        getIntentData()
        setUpViewModel()
        setUpObservers()
    }

    private fun initBindings() {
        binding = ActivityArticlesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getIntentData() {
        intent.extras.let {
            slugId = it?.getString(Constants.SLUGID)
        }
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                ViewModelProviderFactory(ArticlesDetailsViewModel::class) {
                    ArticlesDetailsViewModel(ArticlesDetailsRepository(
                        APIClinet.authApi,
                        ArticlesDatabase(this).articlesDao()),
                        this
                    )
                }
            )
                .get(ArticlesDetailsViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.articlesDetailsLiveData.observe(this, {
            it?.let {
                when (it.status) {
                    ResultStatus.SUCCESS -> {
                        setProgressDialogDisable()
                        setUpFeedArticlesView(it.data?.article)
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

        viewModel.isNetworkAvailable.observe(this, {
            if (!it) {
                showSnackBar()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.checkSlugIdAndLoadArticleDetails(slugId)
    }


    private fun setUpFeedArticlesView(articles: Article?) {
        try {
            articles?.let {
                //binding.authorTv.text = it.author.username
                binding.dateTv.text = DateUtils.convertStringDate(
                    it.createdAt,
                    Constants.InputDateFormate,
                    Constants.OutputDateFormate
                )
                binding.titleEt.setText(it.title)
                binding.bodyEt.setText(it.body)
                binding.descEt.setText(it.description)
               // binding.avatarImgView.loadImage(it.author.image, true)

                binding.titleEt.clearFocus()
            }
        } catch (exception: Exception) {
            Utils.showLogMessage(exception.message)
        }

    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

    private fun showSnackBar() {
        val snackbar: Snackbar = Snackbar
            .make(
                binding.rootLayout,
                resources.getString(R.string.noNetworkConnectedError),
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(Constants.Retry) {
                if (NetworkHelper.isNetworkConnected(this)) {
                    viewModel.checkSlugIdAndLoadArticleDetails(slugId)
                } else {
                    showSnackBar()
                }
            }
        snackbar.setActionTextColor(Color.RED)
        snackbar.show()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}