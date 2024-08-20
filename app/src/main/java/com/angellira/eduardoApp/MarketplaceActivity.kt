package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.angellira.eduardoApp.adapter.Produto
import com.angellira.eduardoApp.adapter.ProductAdapter
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
        setupView()
        setSupportActionBar(binding.myToolbar)
        recyclerView()

        addItem()
    }

    private fun addItem() {
        binding.add.setOnClickListener {
            startActivity(Intent(this, AddMarketItemActivity::class.java))
        }
    }

    private fun recyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(produtoLists)
        recyclerView.adapter = adapter
    }

    private fun setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun binding() {
        binding = ActivityMarketplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_profile -> {
            startActivity(Intent(this,ProfileActivity::class.java))

            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}
