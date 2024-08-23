package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.angellira.eduardoApp.adapter.ProductAdapter
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityMarketplaceBinding
import com.angellira.eduardoApp.model.MarketItem
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MarketplaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarketplaceBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val prefs by lazy { Preferences(this) }
    private lateinit var marketItemDao: MarketItemDao
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase
    private val apiService = ApiServiceFaceBlog.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding()
        setupView()
        database()
        setSupportActionBar(binding.myToolbar)
        lifecycleScope.launch(IO) {
            try{
                val produtoLists = apiService.getItens()
                withContext(Main) {
                    recyclerView(produtoLists)
                }
            }catch (e:Exception){
                val produtoLists = marketItemDao.getAll()
                withContext(Main) {
                    recyclerView(produtoLists)
                }
            }
        }
        addItem()
    }

    private fun addItem() {
        binding.add.setOnClickListener {
            startActivity(Intent(this, AddMarketItemActivity::class.java))
        }
    }

    private fun recyclerView(listPosts: List<MarketItem>) {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this@MarketplaceActivity)

        val adapter = ProductAdapter(listPosts) { _, _, _, _, _, id->
            prefs.idItem = id
            val intent = Intent(this@MarketplaceActivity, DetailedItemActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun database() {
        lifecycleScope.launch(IO) {

            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "faceblog.db"
            ).build()
            marketItemDao = db.marketItemDao()
            userDao = db.userDao()
        }
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
            startActivity(Intent(this, ProfileActivity::class.java))
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}
