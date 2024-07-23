package com.angellira.eduardoApp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.angellira.eduardoApp.R

data class Post(
    val name: String = "",
    val text: String = "",
    val image: Int = 0
)

class PostAdapter(
    private val postList: List<Post>
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
        holder.postName.text = post.name
        holder.postText.text = post.text
        holder.postImage.setImageResource(post.image)
    }

    override fun getItemCount() = postList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postName: TextView = itemView.findViewById(R.id.postName)
        val postText: TextView = itemView.findViewById(R.id.postText)
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
    }
}
