package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityEsqueciMinhaSenhaEnviadoBinding

class EsqueciMinhaSenhaEnviadoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEsqueciMinhaSenhaEnviadoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        setupView()
        setSupportActionBar(binding.myToolbar)
        voltar()
    }

    private fun setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun binding() {
        binding = ActivityEsqueciMinhaSenhaEnviadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun voltar() {
        binding.voltar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.no_icon, menu)
        return true
    }
}