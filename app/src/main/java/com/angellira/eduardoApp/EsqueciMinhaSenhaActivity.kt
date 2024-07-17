package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityEsqueciMinhaSenhaBinding

class EsqueciMinhaSenhaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEsqueciMinhaSenhaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        enviar()
    }

    private fun enviar() {
        binding.enviar.setOnClickListener {
            startActivity(Intent(this, EsqueciMinhaSenhaEnviadoActivity::class.java))
        }
    }

    private fun binding() {
        binding = ActivityEsqueciMinhaSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}