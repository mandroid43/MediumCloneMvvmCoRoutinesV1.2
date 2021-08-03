package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articleList: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article)

    @Query("select * from tbl_article_data")
    fun getArticles(): Flow<List<Article>>

    @Query("SELECT * FROM tbl_article_data WHERE slug = :slug")
    abstract fun getArticle(slug: String): Flow<Article>

    @Query("DELETE FROM tbl_article_data")
     fun deleteAllArticles()
}