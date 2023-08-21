package com.aminivan.newsapi.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aminivan.newsapi.databinding.ItemArticleBinding
import com.aminivan.newsapi.databinding.ItemSourceBinding
import com.aminivan.newsapi.model.ArticlesItemPaging
import com.bumptech.glide.Glide
import javax.inject.Inject

class TopHeadlineAdapterPaging @Inject constructor() : PagingDataAdapter<ArticlesItemPaging, TopHeadlineAdapterPaging.ViewHolder>(
    differCallback
)  {

    private lateinit var context: Context
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(item: ArticlesItemPaging)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    inner class ViewHolder(val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticlesItemPaging){
            binding.apply {
                binding.articleTitle.text = item.title
            }
            Glide.with(context).load(item.urlToImage).into(binding.articleImage)
            binding.cardArticle.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }
    companion object {
        val differCallback = object : DiffUtil.ItemCallback<ArticlesItemPaging>() {
            override fun areItemsTheSame(oldItem: ArticlesItemPaging, newItem: ArticlesItemPaging): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ArticlesItemPaging, newItem: ArticlesItemPaging): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return ViewHolder(binding)
    }
}