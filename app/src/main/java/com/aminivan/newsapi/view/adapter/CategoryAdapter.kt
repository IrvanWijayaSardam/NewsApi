package com.aminivan.newsapi.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aminivan.newsapi.databinding.ItemCategoryBinding
import com.aminivan.newsapi.model.CategoryData
import com.bumptech.glide.Glide


class CategoryAdapter(var listCategory : List<CategoryData>):RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var onClick : ((CategoryData) -> Unit)? = null

    class ViewHolder(var binding : ItemCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var category = listCategory[position]
        holder.binding.categoryName.text = category.name
        Glide.with(holder.itemView).load(category.picture).into(holder.binding.categoryImage)
        holder.binding.itemCategory.setOnClickListener{
            Log.d("cat", category.name)
            Toast.makeText(holder.itemView.context, category.name, Toast.LENGTH_SHORT).show()
            onClick?.invoke(category)
        }
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

}