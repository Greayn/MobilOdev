package com.example.grads_hw.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grads_hw.R
import com.example.grads_hw.models.User
import com.example.grads_hw.databinding.ItemGradBinding

class GradsAdapter(
    private val users: List<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<GradsAdapter.GradsViewHolder>() {

    inner class GradsViewHolder(val binding: ItemGradBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(user: User) {
            binding.apply {
                textViewName.text = "${user.firstName} ${user.lastName}"
                textViewYears.text = "${user.entYear} - ${user.gradYear}"
                Glide.with(root).load(user.imageUrl).placeholder(R.drawable.image_placeholder).into(shapeableImageView)
                root.setOnClickListener {
                    onUserClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradsViewHolder {
        val binding = ItemGradBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GradsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradsViewHolder, position: Int) {
        val user = users[position]
        holder.bindTo(user)
    }

    override fun getItemCount() = users.size

}