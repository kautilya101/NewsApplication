package com.androiddevs.mvvmnewsapp.pack.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androiddevs.mvvmnewsapp.pack.models.Article


@Dao // allows us to access particular data resource without coupling the resource api to business logic something we want to ensure in mvvm
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article) : Long /* A suspending function is simply a
                                                   function that can be paused and
                                                   resumed at a later time. They can
                                                   execute a long running operation and wait
                                                   for it to complete without blocking.
                                                   */

    @Query("SELECT * FROM articles")  // fetch articles from database
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}