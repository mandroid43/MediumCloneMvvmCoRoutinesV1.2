package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao

import androidx.room.*
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articleList: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Query("select * from Article")
    fun getArticles(): Flow<List<Article>>

    @Query("SELECT * FROM Article WHERE slug = :slug")
    fun getArticle(slug: String): Flow<Article>

    @Query("DELETE FROM Article")
    suspend fun deleteAllArticles()

     @Transaction
     @Query("Select * From Article where id=:id")
     fun getById(id:Int) : List<Article>
}