package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.typeConverter.ArticleTypeConverter
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article


data class ArticlesResponse(


    public val id: Int = 0,



    val articles: List<Article>




)