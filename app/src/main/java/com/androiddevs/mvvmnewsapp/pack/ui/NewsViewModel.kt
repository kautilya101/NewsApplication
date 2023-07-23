package com.androiddevs.mvvmnewsapp.pack.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.pack.NewsApplication
import com.androiddevs.mvvmnewsapp.pack.models.Article
import com.androiddevs.mvvmnewsapp.pack.models.NewsResponse
import com.androiddevs.mvvmnewsapp.pack.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.pack.util.Resouce
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app : Application,
    val newsRepository : NewsRepository
) : AndroidViewModel(app) {
    val breakingNews : MutableLiveData<Resouce<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null


    val searchNews : MutableLiveData<Resouce<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("in")
        getSearchNews("")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun getSearchNews(searchQuery : String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }


    private fun handleBreakingNewsResponse(response : Response<NewsResponse>) : Resouce<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = resultResponse
                }
                else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resouce.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resouce.Error(response.message())
    }

    private fun handleSearchNewsResponse(response : Response<NewsResponse>) : Resouce<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }
                else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resouce.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resouce.Error(response.message())
    }

    fun savedNews(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun getallNews() = newsRepository.getSavedNews()

    fun deleteNews(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resouce.Loading())
        try {
            if(hasInternt()){
                val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                breakingNews.postValue(Resouce.Error("No Internet Connection"))
            }
        }
        catch (t : Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resouce.Error("Network Failure"))
                else -> breakingNews.postValue(Resouce.Error("Connection Error"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resouce.Loading())
        try {
            if(hasInternt()){
                val response = newsRepository.getSearchNews(searchQuery,searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }
            else{
                searchNews.postValue(Resouce.Error("No Internet Connection"))
            }
        }
        catch (t : Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resouce.Error("Network Failure"))
                else -> searchNews.postValue(Resouce.Error("Connection Error"))
            }
        }
    }

    private fun hasInternt():Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else ->
                    false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_ETHERNET -> true
                    TYPE_MOBILE -> true
                    else -> false
                }
            }
        }
        return false

    }


}