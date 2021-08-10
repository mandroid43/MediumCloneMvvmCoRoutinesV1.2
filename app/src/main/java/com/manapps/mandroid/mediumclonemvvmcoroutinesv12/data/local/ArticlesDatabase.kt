package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ArticleDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ProfileDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.typeConverter.ArticleTypeConverter
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Profile
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse

@Database(
    entities = [Profile::class, Article::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(ArticleTypeConverter::class)
abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun articlesDao(): ArticleDao
    abstract fun profileDao(): ProfileDao

    companion object {
        private const val DATABASE_NAME = "medium-app"

        @Volatile
        private var INSTANCE: ArticlesDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticlesDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}