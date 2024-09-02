package com.angellira.eduardoApp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.angellira.eduardoApp.R
import com.angellira.eduardoApp.model.MarketItem

class ProductAdapter(
    private val produtoList: List<MarketItem>,
    private val onItemClickListener: (String, String, String, String, String, String) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_item_product,
            parent,
            false
        )
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = produtoList[position]
        holder.productTitle.text = product.title
        holder.productPrice.text = product.price
        holder.productImage.load(product.img)
        holder.productDescription.text = product.description

        holder.itemView.setOnClickListener {
            onItemClickListener(product.img, product.user, product.price, product.title, product.description, product.id.toString())
        }
    }

    override fun getItemCount() = produtoList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productTitle: TextView = itemView.findViewById(R.id.productTitle)
        val productPrice: TextView = itemView.findViewById(R.id.precoDoProduto)
        val productImage: ImageView = itemView.findViewById(R.id.ImagemProduto)
        val productDescription: TextView = itemView.findViewById(R.id.productDesc)


    }
}
