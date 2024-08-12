package com.angellira.eduardoApp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.angellira.eduardoApp.R
import com.angellira.eduardoApp.model.Posts


class PostAdapter(
    private var postList: List<Posts>
) : RecyclerView.Adapter<PostAdapter.ProductViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updatePosts(newPosts: Map<String, Posts>) {
        postList = newPosts.values.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_post,
            parent,
            false
        )
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val post = postList[position]
        holder.postName.text = post.user
        holder.postText.text = post.message
        holder.postImage.load(post.img)
    }

    override fun getItemCount() = postList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postName: TextView = itemView.findViewById(R.id.postName)
        val postText: TextView = itemView.findViewById(R.id.postText)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
    }
}