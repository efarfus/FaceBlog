package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityUrlSetBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UrlSetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUrlSetBinding
    private lateinit var userDao: UserDao
    private val apiService = ApiServiceFaceBlog.retrofitService
    private lateinit var db: AppDatabase
    private var user = User()
    private val prefs by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding()
        setupView()

        val buttonChooseImage = binding.textimagemeditprofile
        val buttonSave = binding.botaoconfirmaredicaoconta

        buttonSave.setOnClickListener {
            binding.textView.visibility = VISIBLE
            binding.progressBar.visibility = VISIBLE
            lifecycleScope.launch(IO) {
                database()
                setUser()
                user.img = buttonChooseImage.text.toString()
                putAll()
                delay(2_000)
                finish()
                startActivity(Intent(this@UrlSetActivity, ProfileActivity::class.java))
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
        binding = ActivityUrlSetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)

    }

    private suspend fun setUser() {
        user = withContext(IO) {
            try {
                apiService.getUserById(prefs.id.toString())
            } catch (e: Exception) {
                userDao.get(prefs.id.toString()) ?: User()
            }
        }
    }

    private suspend fun database() {
        withContext(IO) {
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "faceblog.db"
            ).build()
            userDao = db.userDao()
        }
    }

    private suspend fun putAll() {
        withContext(IO) {
            try {
                apiService.putUser(prefs.id.toString(), user)
                userDao.updateUser(prefs.id.toString(), user.name, user.email, user.password)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
