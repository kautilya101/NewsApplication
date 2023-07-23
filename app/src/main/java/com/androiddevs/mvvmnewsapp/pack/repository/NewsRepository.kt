package com.androiddevs.mvvmnewsapp.pack.repository

import com.androiddevs.mvvmnewsapp.pack.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.pack.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.pack.models.Article

class NewsRepository(
    val db : ArticleDatabase
){
    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun getSearchNews(searchQuery : String, pageNumber : Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)


    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) = db.getArticleDao().deleteArticle(article)



}