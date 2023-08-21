package com.aminivan.newsapi.model

import com.google.gson.annotations.SerializedName

data class ResponseTopHeadline(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItemPaging?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ArticlesItemPaging(

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("urlToImage")
	val urlToImage: Any? = null,

	@field:SerializedName("description")
	val description: Any? = null,

	@field:SerializedName("source")
	val source: SourceData? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("content")
	val content: Any? = null
)

data class SourceData(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
