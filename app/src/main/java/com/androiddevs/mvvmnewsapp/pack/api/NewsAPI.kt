package com.androiddevs.mvvmnewsapp.pack.api

import com.androiddevs.mvvmnewsapp.pack.models.NewsResponse
import com.androiddevs.mvvmnewsapp.pack.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(   // using  async call
        @Query("country")
        countryCode: String  = "in",
        @Query("page")
        pageNumber :Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY

    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber :Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY

    ): Response<NewsResponse>

}