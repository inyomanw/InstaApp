package com.inyomanw.instaapp.presentation.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inyomanw.instaapp.R
import com.inyomanw.instaapp.databinding.ItemPostBinding
import com.inyomanw.instaapp.domain.model.PostDomain

class PostsAdapter : ListAdapter<PostDomain, PostsAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(
        private val binding: ItemPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostDomain) {
            binding.apply {
                tvUserName.text = post.userName

                val captionText = "${post.userName} ${post.caption}"
                tvCaption.text = captionText

                Glide.with(itemView.context)
                    .load(post.userPhotoUrl)
                    .placeholder(R.drawable.ic_image)
                    .circleCrop()
                    .into(ivUserAvatar)

                Glide.with(itemView.context)
                    .load(post.imageUrl)
                    .placeholder(android.R.color.darker_gray)
                    .into(ivPostImage)
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<PostDomain>() {
        override fun areItemsTheSame(oldItem: PostDomain, newItem: PostDomain): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: PostDomain, newItem: PostDomain): Boolean {
            return oldItem == newItem
        }
    }
}