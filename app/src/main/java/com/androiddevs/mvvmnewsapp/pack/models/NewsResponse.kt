package com.androiddevs.mvvmnewsapp.pack.models

import com.androiddevs.mvvmnewsapp.pack.models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)