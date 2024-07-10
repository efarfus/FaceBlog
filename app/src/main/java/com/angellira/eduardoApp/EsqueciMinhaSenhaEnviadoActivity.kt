package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
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
        setContentView(R.layout.activity_esqueci_minha_senha_enviado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding()
        voltar()
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
}