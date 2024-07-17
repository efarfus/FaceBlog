package com.angellira.eduardoApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.eduardoApp.model.Produto
import com.angellira.eduardoApp.model.ProductAdapter
import com.angellira.eduardoApp.databinding.ActivityMarketplaceBinding


class MarketplaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketplaceBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val produtoLists = listOf(
        Produto("Chevette", "R$17.000,00", R.drawable.chevette),
        Produto("Opala", "R$20.000,00", R.drawable.opala),
        Produto("Gol quadrado", "R$10.000,00", R.drawable.golquadrado)
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(produtoLists)
        recyclerView.adapter = adapter
    }

    private fun binding() {
        binding = ActivityMarketplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
