package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities


import androidx.room.*

@Entity/*(
    foreignKeys = [
        ForeignKey(
        entity = Profile::class,
        parentColumns = ["user_id"],
        childColumns = ["authorId"]
    )
    ],
    indices =[Index(value = ["authorId"])]
)*/
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //val authorId:Int,
    @Embedded(prefix = "author_")

    val author: Profile,
    val body: String,
    val description: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    val slug: String,
    //    val tagList: List<String>,
    val title: String,
    val updatedAt: String,
    val createdAt: String
)

