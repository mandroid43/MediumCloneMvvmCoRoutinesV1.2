package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_article_data")

data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val author: Profile,
    val body: String,
    val createdAt: String,
    val description: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    val slug: String,
    //    val tagList: List<String>,
    val title: String,
    val updatedAt: String
)

