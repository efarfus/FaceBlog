package com.angellira.eduardoApp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.angellira.eduardoApp.databinding.ActivityCadastroBinding
import com.angellira.eduardoApp.databinding.ActivityDetailedPostBinding
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.network.ApiService
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.launch

class DetailedPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedPostBinding
    private val apiService = ApiServiceFaceBlog.retrofitService
    private val prefs by lazy { Preferences(this) }
    private var post = Posts("","","","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detailed_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding()


        lifecycleScope.launch {
            post = apiService.getPostId(prefs.idPost.toString())
            binding.nameUser.text = post.user
            binding.textUser.text = post.message
            binding.imageUser.load(post.img)
        }


    }

    private fun binding() {
        binding = ActivityDetailedPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}