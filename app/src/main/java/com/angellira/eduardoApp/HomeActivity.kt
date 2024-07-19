package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityMainBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.preferences.Preferences

class MainActivity : AppCompatActivity() {
    private val user = User()
    private lateinit var binding: ActivityMainBinding
    private val prefs by lazy { Preferences(this) }

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


        user.name = prefs.name.toString()
        val bemVindoBox = binding.bemVindo

        val marketplaceActivity = Intent(this, MarketplaceActivity::class.java)
        val profileActivity = Intent(this, ProfileActivity::class.java)

        mensagem(bemVindoBox)

        marketplace(marketplaceActivity)

        perfil(profileActivity)
    }

    private fun mensagem(bemVindoBox: TextView) {
        bemVindoBox.setText("Bem vindo, ${user.name}!")
    }

    private fun perfil(profileActivity: Intent) {
        binding.profile1.setOnClickListener {
            startActivity(profileActivity)
        }
    }

    private fun marketplace(marketplaceActivity: Intent) {
        binding.options.setOnClickListener {
            startActivity(marketplaceActivity)
        }
    }

}