package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import com.angellira.eduardoApp.databinding.ActivityDetailedPostBinding
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedPostBinding
    private val prefs by lazy { Preferences(this) }
    private var post = Posts()
    private var user = User()
    private lateinit var db: AppDatabase
    private lateinit var postsDao: PostsDao
    private lateinit var userDao: UserDao
    private val apiService = ApiServiceFaceBlog.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        setupView()
        setSupportActionBar(binding.myToolbar)
        database()
        lifecycleScope.launch(IO) {
            user = setUser()
            post = loadPost()
            moreOptions()
        }

        closeDelete()
        createDialogDelete()

    }

    private fun createDialogDelete() {
        binding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Você tem certeza de que deseja excluir esse post?")
                .setMessage("Após a exclusão não será possível a recuperação de nenhum dado salvo.")
                .setPositiveButton("Sim") { _, _ ->
                    deletePost()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun closeDelete() {
        binding.main.setOnClickListener {
            binding.deleteButton.visibility = INVISIBLE
        }
    }

    private suspend fun setUser(): User {
        return withContext(IO) {
            try {
                apiService.getUserById(prefs.id.toString())
            }catch (e:Exception){
                userDao.get(prefs.id.toString()) ?: User()
            }
        }
    }

    private fun deletePost() {
        lifecycleScope.launch(IO) {
            apiService.deletePost(prefs.idPost.toString())
            postsDao.delete(post)
            withContext(Main) {
                binding.deleteButton.visibility = INVISIBLE
                startActivity(Intent(this@DetailedPostActivity, MainActivity::class.java))
            }
        }
    }

    private fun moreOptions() {
        if (post.user == user.name) {
            binding.moreOptions.visibility = VISIBLE
            binding.moreOptions.setOnClickListener {
                binding.deleteButton.visibility = VISIBLE
            }
        }
    }

    private suspend fun loadPost(): Posts {
        return withContext(IO) {
            try {
                val loadedPost = apiService.getPostId(prefs.idPost.toString())
                withContext(Main) {
                    binding.nameUser.text = loadedPost.user
                    binding.textUser.text = loadedPost.message
                    binding.imageUser.load(loadedPost.img)
                }
                loadedPost
            }catch (e:Exception){
                val loadedPost = postsDao.get(prefs.idPost.toString()) ?: Posts()
                withContext(Main) {
                    binding.nameUser.text = loadedPost.user
                    binding.textUser.text = loadedPost.message
                    binding.imageUser.load(loadedPost.img)
                }
                loadedPost
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


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }
}

