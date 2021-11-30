package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


<<<<<<< HEAD
@Entity
=======
//@Entity(foreignKeys = arrayOf(
//    ForeignKey(entity = Article::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("user_id"),
//        onDelete = ForeignKey.CASCADE)
//))
>>>>>>> origin/master
data class Profile(
   @PrimaryKey(autoGenerate = true)
    val user_id: Int=0,
    val bio: String?,
    val following: Boolean,
    val image: String,
    val username: String
)