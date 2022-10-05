package com.batara.gtihubuserdicoding.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batara.gtihubuserdicoding.UsersResponseItem
import com.batara.gtihubuserdicoding.databinding.ListUserGithubBinding
import com.bumptech.glide.Glide

class ListFollowAdapter(private val listUser : ArrayList<UsersResponseItem>) : RecyclerView.Adapter<ListFollowAdapter.ViewHolder>() {
    class ViewHolder(var binding: ListUserGithubBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListUserGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUsername.text = listUser[position].login
        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .circleCrop()
            .into(holder.binding.imgItemAvatar)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}