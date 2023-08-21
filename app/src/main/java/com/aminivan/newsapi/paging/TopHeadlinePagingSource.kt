package com.aminivan.newsapi.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.RecyclerView
import com.aminivan.newsapi.api.ApiService
import com.aminivan.newsapi.databinding.ItemSourceBinding
import com.aminivan.newsapi.model.ArticlesItemPaging
import retrofit2.HttpException

class TopHeadlinePagingSource(private val apiService: ApiService,private val category:String,private val query:String) :
    PagingSource<Int, ArticlesItemPaging>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItemPaging> {
        return try {
            val currentPage = params.key ?: 1
            val prevPage = currentPage - 1
            val nextPage = currentPage + 1

            val response = apiService.fetchTopHeadlines(category,"id","f136c3cd54824b699ec80b233a64f6ed",20,currentPage,query)

            if(response.code() >= 400)
                throw Exception("Bad Request (${response.code()}), Silahkan mencoba lagi")
            else if(!response.isSuccessful)
                throw Exception("Sistem kami mendeteksi jaringan Anda berada pada frekuensi internet berkecepatan rendah." +
                        "\nPastikan Anda menggunakan internet berkecepatan tinggi (4G) untuk menghidari ketidaksesuaian data.")

            val data = response.body()?.articles.orEmpty() // Convert nullable list to non-null list
            val responseData = mutableListOf<ArticlesItemPaging>()
            val maxPage = response.body()!!.totalResults!!.toInt() / 20

            if(response.body()!!.totalResults!!.toInt() != 0){
                responseData.clear()
                responseData.addAll(data.filterNotNull())
                if (currentPage < maxPage){
                    return LoadResult.Page(
                        data = responseData,
                        prevKey = if (currentPage == 1) null else prevPage,
                        nextKey = nextPage
                    )
                } else if (currentPage == maxPage){
                    return LoadResult.Page(
                        data = responseData,
                        prevKey = if (currentPage == 1) null else prevPage,
                        nextKey = null
                    )
                }
            } else {
                responseData.clear()
                return LoadResult.Page(
                    data = responseData,
                    prevKey = null,
                    nextKey = null
                )
            }
            return LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else prevPage,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ArticlesItemPaging>): Int? {
        return null
    }
}