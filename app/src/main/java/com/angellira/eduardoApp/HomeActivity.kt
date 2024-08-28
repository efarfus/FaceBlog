package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.angellira.eduardoApp.adapter.PostAdapter
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.PostsDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityMainBinding
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID.randomUUID

class MainActivity : AppCompatActivity() {
    private var user = User()
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val prefs by lazy { Preferences(this) }
    private lateinit var db: AppDatabase
    private lateinit var postsDao: PostsDao
    private lateinit var userDao: UserDao
    private val apiService = ApiServiceFaceBlog.retrofitService

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setSupportActionBar(binding.myToolbar)

        lifecycleScope.launch(IO) {
            database()
            setUser()
            loadPosts()
            withContext(Main)
            {
                marketplace(Intent(this@MainActivity, MarketplaceActivity::class.java))
                postar()
            }
        }
    }

    private fun database() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "faceblog.db"
        ).build()
        postsDao = db.postsDao()
        userDao = db.userDao()
    }

    private fun postar() {
        binding.enviarPost.setOnClickListener {
            lifecycleScope.launch {
                withContext(IO) {
                    setUser()

                    if (binding.caixaPost.text.toString().isNotEmpty()) {
                        user.id = randomUUID().toString()
                        val post =
                            Posts(user.id, user.name, binding.caixaPost.text.toString(), user.img)
                        apiService.savePost(post)
                        postsDao.insert(post)
                        loadPosts()
                    }
                }

                withContext(Main) {
                    binding.caixaPost.text.clear()
                    hideKeyboard(this@MainActivity, currentFocus ?: View(this@MainActivity))
                    Toast.makeText(
                        this@MainActivity,
                        "Post carregado com sucesso",
                        Toast.LENGTH_LONG
                    ).show()
                    loadPosts()
                }
            }
        }
    }

    private fun loadPosts() {
        lifecycleScope.launch(IO) {
            try {
                val postsList = apiService.getPosts()
                withContext(Main) {
                    recyclerView(postsList.reversed())
                }
            } catch (e: Exception) {
                val postsList = postsDao.getAll()
                withContext(Main) {
                    recyclerView(postsList.reversed())
                }
            }

        }
    }

    private fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private suspend fun setUser() = withContext(IO) {

        try {
            val userEntity = apiService.getUserById(prefs.id.toString())
            user.name = userEntity.name
            user.img = userEntity.img
            withContext(Main) {
                binding.caixaPost.hint = "No que você está pensando, ${user.name}?"
            }
        } catch (e: Exception) {
            user = userDao.get(prefs.id.toString())!!
            withContext(Main) {
                binding.caixaPost.hint = "No que você está pensando, ${user.name}?"
            }
        }

    }


    private fun recyclerView(listPosts: List<Posts>) {
        recyclerView = binding.recyclerViewPosts
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PostAdapter(listPosts) { _, _, id, _ ->
            prefs.idPost = id
            val intent = Intent(this@MainActivity, DetailedPostActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
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

        else -> super.onOptionsItemSelected(item)
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
