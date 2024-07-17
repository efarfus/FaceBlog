package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        user.name = sharedPreferences.getString("name", user.name).toString()
        val bemVindoBox = binding.bemVindo

        val MarketplaceActivity = Intent(this, MarketplaceActivity::class.java)
        val ProfileActivity = Intent(this, ProfileActivity::class.java)

        mensagem(bemVindoBox)

        marketplace(MarketplaceActivity)

        perfil(ProfileActivity)
    }

    private fun mensagem(bemVindoBox: TextView) {
        bemVindoBox.setText("Bem vindo, ${user.name}!")
    }

    private fun perfil(ProfileActivity: Intent) {
        binding.profile1.setOnClickListener {
            startActivity(ProfileActivity)
        }
    }

    private fun marketplace(MarketplaceActivity: Intent) {
        binding.options.setOnClickListener {
            startActivity(MarketplaceActivity)
        }
    }

}