package com.angellira.eduardoApp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.load
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.PostsDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityCadastroBinding
import com.angellira.eduardoApp.databinding.ActivityDetailedPostBinding
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.network.ApiService
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedPostBinding
    private val apiService = ApiServiceFaceBlog.retrofitService
    private val prefs by lazy { Preferences(this) }
    private var post = Posts("1", "", "", "")
    private lateinit var db: AppDatabase
    private lateinit var postsDao: PostsDao
    private lateinit var userDao: UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        setupView()


        database()
        loadPost()
    }

    private fun loadPost() {
        lifecycleScope.launch(IO) {
            post = postsDao.get(prefs.idPost.toString())!!
            withContext(Main) {
                binding.nameUser.text = post.user
                binding.textUser.text = post.message
                binding.imageUser.load(post.img)
            }
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
        binding = ActivityDetailedPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun database() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "faceblog.db"
        ).build()
        postsDao = db.postsDao()
        userDao = db.userDao()
    }
}