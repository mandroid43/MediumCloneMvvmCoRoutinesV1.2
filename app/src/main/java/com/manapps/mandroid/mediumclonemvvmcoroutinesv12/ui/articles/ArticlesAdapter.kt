package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Constants
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Utils
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.articles.viewArticles.ArticlesDetailsActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.databinding.ListItemArticleBinding
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.DateUtils

class ArticlesAdapter(
    private val context: Context?,
    private val articlesList: List<Article>
) :
    RecyclerView.Adapter<ArticlesAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ListItemArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = articlesList.size
    class ItemViewHolder(private val holderBinding: ListItemArticleBinding) :
        RecyclerView.ViewHolder(holderBinding.root) {
        fun bind(article: Article) {
            try {
                holderBinding.apply {
                    authorTv.text = article.author.username
                    titleTv.text = article.title
                    bodyDescTv.text = article.body
                    avatarImgView.loadImage(article.author.image, true)
                    dateTv.text = DateUtils.convertStringDate(article.createdAt,Constants.InputDateFormate,Constants.OutputDateFormate)
                }
            } catch (exception: Exception) {
                Utils.showLogMessage(exception.message)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(articlesList[position])
        holder.itemView.setOnClickListener {
            Utils.moveToWithData(
                context,
                ArticlesDetailsActivity::class.java, Constants.SLUGID, articlesList[position].slug
            )
        }
    }
}