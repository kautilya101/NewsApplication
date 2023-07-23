package com.androiddevs.mvvmnewsapp.pack.db

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.pack.models.Source

class Convertors {

    @TypeConverter
    fun fromSource(source: Source) : String?{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : Source {
        return Source(name,name)
    }
}