package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.typeConverter.ArticleTypeConverter
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article

@Entity(tableName = "tbl_article_data")
data class ArticlesResponse(
    @PrimaryKey
    public val id: Int = 1,

    @ColumnInfo(name = "article_response")
    @TypeConverters(ArticleTypeConverter::class)
    val articles: List<Article>

    //, val articlesCount: Int


)