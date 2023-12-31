package com.example.submission1storyapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1storyapp.data.database.Entities
import com.example.submission1storyapp.data.response.ListStoryItem
import com.example.submission1storyapp.databinding.ItemStoryBinding

class StoryListAdapter(private val onItemClickListener: OnItemClickListener) :
    PagingDataAdapter<Entities, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickListener {
        fun onItemClick(item: Entities)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(
        private val binding: ItemStoryBinding,
        private val onItemClickListener: OnItemClickListener

    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Entities) {
            binding.TvTitle.text = "${review.sender}"
            binding.TvDes.text = review.description
            Glide.with(binding.root.context)
                .load(review.photoUrl)
                .into(binding.imgView)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.imgView, "profile"),
                    Pair(binding.TvTitle, "name"),
                    Pair(binding.TvDes, "description")
                )

            itemView.setOnClickListener {
                onItemClickListener.onItemClick(review)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Entities>() {
            override fun areItemsTheSame(oldItem: Entities, newItem: Entities): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Entities,
                newItem: Entities,
            ): Boolean {
                return oldItem.idStory == newItem.idStory
            }
        }
    }
}