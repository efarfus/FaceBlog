package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.eduardoApp.adapter.PostAdapter
import com.angellira.eduardoApp.databinding.ActivityMainBinding
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private var user = User()
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val prefs by lazy { Preferences(this) }
    private val apiService = ApiServiceFaceBlog.retrofitService


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setSupportActionBar(binding.myToolbar)
        lifecycleScope.launch {
            setUser()
        }
        marketplace(Intent(this, MarketplaceActivity::class.java))
        recyclerView()
        postar()
    }

    private fun postar() {
        binding.enviarPost.setOnClickListener {
            if (user.img.isEmpty())
            {
                user.img = "https://static.vecteezy.com/system/resources/thumbnails/005/129/844/small_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg"
            }
            if (binding.caixaPost.text.toString().isNotEmpty())

            lifecycleScope.launch {
                val randomId = UUID.randomUUID().toString()
                val post = Posts(randomId, user.name, binding.caixaPost.text.toString(), user.img)
                apiService.savePost(post)
                binding.caixaPost.text.clear()
                Toast.makeText(this@MainActivity, "Post carregado com sucesso", Toast.LENGTH_LONG)
                    .show()
                recyclerView()
            }
            else
            {
                Toast.makeText(this@MainActivity, "Post vazio", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun loadPosts(adapter: PostAdapter) {
        lifecycleScope.launch {
            val postList = apiService.getPosts()
            Log.e("PostList", postList.toString())
            adapter.updatePosts(postList)
        }
    }

        private suspend fun setUser() {
            user = apiService.getUser(prefs.id.toString())
            Log.e("userRecebe", user.name)
            binding.caixaPost.hint = "No que você está pensando, ${user.name}?"
        }

        private fun recyclerView() {
            recyclerView = binding.recyclerViewPosts
            recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = PostAdapter(emptyList())
            recyclerView.adapter = adapter
            loadPosts(adapter)
        }

        private fun setupView() {
            enableEdgeToEdge()
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
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

        private fun marketplace(marketplaceActivity: Intent) {
            binding.options.setOnClickListener {
                startActivity(marketplaceActivity)
            }
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.itens, menu)
            return true
        }

    }