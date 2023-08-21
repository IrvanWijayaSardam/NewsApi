package com.aminivan.newsapi.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aminivan.newsapi.databinding.ItemSourceBinding
import com.aminivan.newsapi.model.ArticlesItemPaging
import com.aminivan.newsapi.paging.TopHeadlinePagingSource
import javax.inject.Inject

class TopHeadlineAdapterPaging @Inject constructor() : PagingDataAdapter<ArticlesItemPaging, TopHeadlineAdapterPaging.ViewHolder>(
    differCallback
)  {

    private lateinit var context: Context

    inner class ViewHolder(val binding: ItemSourceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticlesItemPaging){
            binding.apply {
                nameSource.text = item.title
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
        val binding = ItemSourceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return ViewHolder(binding)
    }


}