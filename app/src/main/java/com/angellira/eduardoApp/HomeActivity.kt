package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityMainBinding
import com.angellira.eduardoApp.model.User

class MainActivity : AppCompatActivity() {
    private val user = User()
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        user.name = sharedPreferences.getString("name", null).toString()
        val bemVindoBox = binding.bemVindo

        val MarketplaceActivity = Intent(this, MarketplaceActivity::class.java)
        val ProfileActivity = Intent(this, ProfileActivity::class.java)

        bemVindoBox.setText("Bem vindo, ${user.name}!")

        binding.options.setOnClickListener {
            startActivity(MarketplaceActivity)
        }

        binding.profile1.setOnClickListener {
            startActivity(ProfileActivity)
        }
    }

}