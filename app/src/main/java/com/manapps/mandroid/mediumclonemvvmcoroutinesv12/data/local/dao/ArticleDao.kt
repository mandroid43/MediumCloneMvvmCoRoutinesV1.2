package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articlesResponse: ArticlesResponse)

    @Query("select * from tbl_article_data")
    fun getArticles():ArticlesResponse
}