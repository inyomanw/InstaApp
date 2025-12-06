package com.inyomanw.instaapp.presentation.ui.comments


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inyomanw.instaapp.R
import com.inyomanw.instaapp.domain.model.CommentDomain
import com.inyomanw.instaapp.databinding.ItemCommentBinding

class CommentsAdapter(
    private val currentUserId: String,
    private val onDeleteClick: (CommentDomain) -> Unit
) : ListAdapter<CommentDomain, CommentsAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: CommentDomain) {
            binding.apply {
                tvUserName.text = comment.userName
                tvComment.text = comment.text

                btnDelete.isVisible = comment.userId == currentUserId

                Glide.with(itemView.context)
                    .load(comment.userPhotoUrl)
                    .placeholder(R.drawable.ic_image)
                    .circleCrop()
                    .into(ivUserAvatar)

                btnDelete.setOnClickListener {
                    onDeleteClick(comment)
                }
            }
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<CommentDomain>() {
        override fun areItemsTheSame(oldItem: CommentDomain, newItem: CommentDomain): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: CommentDomain, newItem: CommentDomain): Boolean {
            return oldItem == newItem
        }
    }
}
