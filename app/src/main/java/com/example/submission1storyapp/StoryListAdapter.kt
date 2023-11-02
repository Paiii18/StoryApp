package com.example.submission1storyapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1storyapp.data.response.ListStoryItem
import com.example.submission1storyapp.databinding.ItemStoryBinding
import com.example.submission1storyapp.view.detailstory.DetailStoryActivity

class StoryListAdapter (private val onItemClickListener: OnItemClickListener) :
    ListAdapter<ListStoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onItemClick(item: ListStoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,onItemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
    class MyViewHolder(
        private val binding: ItemStoryBinding,
        private val onItemClickListener: OnItemClickListener

    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListStoryItem) {
            binding.TvTitle.text = "${review.name}"
            binding.TvDes.text = review.description
            Glide.with(binding.root.context)
                .load(review.photoUrl)
                .into(binding.imgView)

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(review)
            }
        }
    }

    companion object {
        const val TAG = "UserAdapter"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}