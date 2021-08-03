package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.feedArticles


import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.APIClinet
import com.manapps.mandroid.mediumclonemvckotlin.data.repository.FeedArticlesRepository
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.R
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.ArticlesDatabase
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.databinding.ActivityMainBinding
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.createArticles.CreateArticleActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.ArticlesAdapter
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var viewModel: FeedArticlesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        setUpToolBar()
        initFeedArticlesRecyclerView()
        setUpViewModel()
        setUpObservers()
        binding.fab.setOnClickListener { goToCreateArticlesActivity() }
    }


    private fun initBindings() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolBar)
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                ViewModelProviderFactory(FeedArticlesViewModel::class) {
                    FeedArticlesViewModel(
                        FeedArticlesRepository(
                            APIClinet.authApi,
                            ArticlesDatabase.getInstance(this).articlesDao()
                        ), this
                    )
                }
            )
                .get(FeedArticlesViewModel::class.java)
    }

    private fun setUpObservers() {


        viewModel.feedArticles.observe(this, {
            it?.let {
                setUpFeedArticlesView(it)
            }
        })



        viewModel.feedArticlesLiveData.observe(this, {
            it?.let {
                when (it.status) {
                    ResultStatus.SUCCESS -> {
                        setProgressDialogDisable()
                    }
                    ResultStatus.ERROR -> {
                        setProgressDialogDisable()
                        showError(it.message)
                    }
                    ResultStatus.LOADING -> {
                        setProgressDialogEnable()
                    }
                }
            }
        })

        viewModel.isNetworkAvailable.observe(this, {
            if (!it) {
               // showSnackBar()
            }
        })

    }

    private fun initFeedArticlesRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager
    }


    private fun setUpFeedArticlesView(articlesList: List<Article>?) {
        if (articlesList.isNullOrEmpty()) {
            setNoFeedArticlesLayout()
        } else {
            setFeedArticlesViewsVisibility()
            setUpFeedArticlesRecyclerView(articlesList)
        }
    }

    private fun setNoFeedArticlesLayout() {
        setNoFeedArticlesViewsVisibility()
        binding.subHeadingTv.text = resources.getString(R.string.noPostsFoundLabel)
    }

    private fun setNoFeedArticlesViewsVisibility() {
        Utils.setVisibilityGone(binding.recyclerView)
        Utils.setVisibilityVisible(binding.subHeadingTv)
    }

    private fun setFeedArticlesViewsVisibility() {
        Utils.setVisibilityGone(binding.subHeadingTv)
        Utils.setVisibilityVisible(binding.recyclerView)
    }

    private fun setUpFeedArticlesRecyclerView(articlesList: List<Article>) {
        articlesAdapter = ArticlesAdapter(this, articlesList)
        binding.recyclerView.adapter = articlesAdapter
        articlesAdapter.notifyDataSetChanged()
    }

    private fun showError(message: String?) {
        Utils.showMessage(this, message)
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

    private fun goToCreateArticlesActivity() {
        Utils.moveTo(this, CreateArticleActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkNetworkAndLoadFeedArticles()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //  R.id.profileMenuItem -> viewModel.gotoProfile() //need to add this feature soon.
            R.id.logoutMenuItem -> viewModel.logOutSession()
        }
        return super.onOptionsItemSelected(item)
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
                    viewModel.checkNetworkAndLoadFeedArticles()
                } else {
                    showSnackBar()
                }
            }
        snackbar.setActionTextColor(Color.RED)
        snackbar.show()
    }
}