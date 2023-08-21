package com.aminivan.newsapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.aminivan.newsapi.api.ApiService
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

    val category = MutableStateFlow(value = "")
    val search = MutableStateFlow(value = "")


    @OptIn(ExperimentalCoroutinesApi::class)
    var topheadline = category.flatMapLatest{ query ->
        Pager(PagingConfig(1)) { TopHeadlinePagingSource(apiService,query,search.value) }.flow
    }
}