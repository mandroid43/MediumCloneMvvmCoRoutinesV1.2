package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.typeConverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import java.lang.reflect.Type

object ArticleTypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromList(value: List<Article>) = Gson().toJson(value)

    @TypeConverter
    @JvmStatic
    fun toList(value: String): List<Article> {
        val listType: Type = object : TypeToken<List<Article>>() {}.type
        return Gson().fromJson(value, listType)
    }

}