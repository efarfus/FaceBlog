package com.angellira.eduardoApp.adapter

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
    private var postList: List<Posts>,
    private val onItemClickListener: (String, String, String, String) -> Unit
) : RecyclerView.Adapter<PostAdapter.ProductViewHolder>() {


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

        holder.itemView.setOnClickListener {
            onItemClickListener(post.img, post.user, post.id, post.message)
        }
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
