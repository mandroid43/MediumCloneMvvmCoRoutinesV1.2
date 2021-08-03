package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ArticleDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.typeConverter.ArticleTypeConverter
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse

@Database(entities = [Article::class], version = 1,exportSchema = false)
//@TypeConverters(ArticleTypeConverter::class)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun articlesDao(): ArticleDao
    companion object {
        private const val DATABASE_NAME = "medium-app"
        @Volatile
        private var INSTANCE:ArticlesDatabase?=null
        fun getInstance(context: Context):ArticlesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance==null) {
                    instance = Room.databaseBuilder(context.applicationContext, ArticlesDatabase::class.java, DATABASE_NAME).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}