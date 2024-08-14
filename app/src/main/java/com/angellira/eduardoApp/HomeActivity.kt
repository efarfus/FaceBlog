package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import androidx.room.Room
import com.angellira.eduardoApp.adapter.PostAdapter
import com.angellira.eduardoApp.adapter.Produto
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.PostsDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityMainBinding
import com.angellira.eduardoApp.model.MarketItem
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var user = User()
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val prefs by lazy { Preferences(this) }
    private lateinit var db: AppDatabase
    private lateinit var postsDao: PostsDao
    private lateinit var marketItemDao: MarketItemDao
    private lateinit var userDao: UserDao


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setSupportActionBar(binding.myToolbar)
        val produtoLists = listOf(
            MarketItem(1, "Bruno Henrique","R.drawable.chevette", "R$17.000,00", "Chevette", "Chevette a venda" ),
        )



        lifecycleScope.launch(IO) {

            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "faceblog.db"
            ).build()
            postsDao = db.postsDao()
            marketItemDao = db.marketItemDao()
            userDao = db.userDao()


            loadPosts()
            withContext(Main)
            {
                marketplace(Intent(this@MainActivity, MarketplaceActivity::class.java))
                postar() //até aqui tudo ok. postar está quebrado
            }

        }
//        setUser()

    }

    private fun postar() {
        binding.enviarPost.setOnClickListener {
            if (user.img.isEmpty()) {
                user.img =
                    "https://static.vecteezy.com/system/resources/thumbnails/005/129/844/small_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg"
            }
            if (binding.caixaPost.text.toString().isNotEmpty()) {
                lifecycleScope.launch(IO) {
                    val post =
//                        Posts(user.id, user.name, binding.caixaPost.text.toString(), user.img)
//                    postsDao.insert(post)
                    Toast.makeText(
                        this@MainActivity,
                        "Post carregado com sucesso",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    loadPosts()
                }
            } else {
                Toast.makeText(this@MainActivity, "Post vazio", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun loadPosts() {
        lifecycleScope.launch(IO){
            val postsList = postsDao.getAll()
            withContext(Main){
                recyclerView(postsList)
            }
        }
    }

    private fun setUser() {
        lifecycleScope.launch(IO) {
            user.name = postsDao.get(prefs.id.toString())?.user.toString()
            withContext(Main) {
                binding.caixaPost.hint = "No que você está pensando, ${user.name}?"
            }

        }
    }

    private fun recyclerView(listPosts: List<Posts>) {
        recyclerView = binding.recyclerViewPosts
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PostAdapter(listPosts) { imgSrc, name, id, desc ->
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
