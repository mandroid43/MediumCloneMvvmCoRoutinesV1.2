package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities
data class Article(
    val id: Int,
    val author: Profile,
    val body: String,
    val createdAt: String,
    val description: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    val slug: String,
    val tagList: List<String>,
    val title: String,
    val updatedAt: String
)