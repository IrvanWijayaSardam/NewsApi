package com.aminivan.newsapi.api


import com.aminivan.newsapi.model.ResponseSource
import com.aminivan.newsapi.model.ResponseTopHeadline
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("sources") category : String,
        @Query("apiKey") apiKey : String = "f136c3cd54824b699ec80b233a64f6ed",
        @Query("pageSize")  pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("q") search: String,
    ) : Response<ResponseTopHeadline>

    @GET("top-headlines/sources")
    fun getAllSources(
        @Query("category") category : String ,
        @Query("q") query: String,
        @Query("apiKey") apiKey : String = "e604f4b947784474a99a6dcecce2599e"
    ) : Call<ResponseSource>
}
