package com.example.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsRVAdapter(private val listener: NewsItemClicked)
    : RecyclerView.Adapter<NewsRVAdapter.NewsViewHolder>() {

    private val news = ArrayList<News>()

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.textView)
        val description: TextView = itemView.findViewById(R.id.textView2)
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked( news[viewHolder.adapterPosition] )
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentNews = news[position]
        Glide.with(holder.itemView.context).load(currentNews.urlToImage).into(holder.image)
        holder.title.text = currentNews.title
        holder.description.text = currentNews.description
    }

    override fun getItemCount(): Int {
        return news.size
    }

    fun updateNews(updatedNews: ArrayList<News>){
        news.clear()
        news.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

interface NewsItemClicked{
    fun onItemClicked(item: News)
}