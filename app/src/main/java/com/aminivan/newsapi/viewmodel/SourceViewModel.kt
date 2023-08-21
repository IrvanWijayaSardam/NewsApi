package com.aminivan.newsapi.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.aminivan.newsapi.api.ApiService
import com.aminivan.newsapi.model.ResponseSource
import com.aminivan.newsapi.model.SourcesItem
import com.aminivan.newsapi.paging.TopHeadlinePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    var apiService: ApiService
) : ViewModel() {

    lateinit var liveDataSource : MutableLiveData<List<SourcesItem>?>

    val category = MutableStateFlow(value = "")
    val search = MutableStateFlow(value = "")


    init {
        liveDataSource = MutableLiveData()
    }

    fun getDataSource(): MutableLiveData<List<SourcesItem>?> {
        return  liveDataSource
    }

    fun callApiSource(categ : String, context: Context){
        apiService.getAllSources(categ)
            .enqueue(object : Callback<ResponseSource>{
                override fun onResponse(
                    call: Call<ResponseSource>,
                    response: Response<ResponseSource>
                ) {
                    if (response.isSuccessful){
                        liveDataSource.postValue(response.body()!!.sources)
                        Log.d(ContentValues.TAG, "onResponse: ${response.body()!!.sources!!.size}")
                    }else{
                        Log.d(ContentValues.TAG, "onResponse: Unsuccessfully")
                        liveDataSource.postValue(null)
                    }
                }

                override fun onFailure(call: Call<ResponseSource>, t: Throwable) {
                    Log.d(ContentValues.TAG, "onFailure: ${t.message}")
                    liveDataSource.postValue(null)
                }

            })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    var topheadline = category.flatMapLatest{ query ->
        Pager(PagingConfig(1)) { TopHeadlinePagingSource(apiService,query,search.value) }.flow
    }
}