package com.batara.gtihubuserdicoding.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity
import com.batara.gtihubuserdicoding.databinding.ListUserGithubBinding
import com.bumptech.glide.Glide

class ListUserAdapter (private val onBookmarkClick : (UsersEntity) -> Unit) : ListAdapter<UsersEntity, ListUserAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClickClicked(data: UsersEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ListUserGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users : UsersEntity) {
            binding.tvUsername.text = users.username
            Glide.with(itemView.context)
                .load(users.urlImage)
                .circleCrop()
                .into(binding.imgItemAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =ListUserGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UsersEntity> =
            object : DiffUtil.ItemCallback<UsersEntity>() {
                override fun areItemsTheSame(oldUser: UsersEntity, newUser: UsersEntity): Boolean {
                    return oldUser.username == newUser.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UsersEntity, newUser: UsersEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = getItem(position)
        holder.bind(users)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClickClicked(getItem(position))
        }
    }
}