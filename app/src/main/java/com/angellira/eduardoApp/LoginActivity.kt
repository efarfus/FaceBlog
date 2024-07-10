package com.angellira.eduardoApp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityLoginBinding
import com.angellira.eduardoApp.model.User

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding()
        val pagMain = Intent(this, MainActivity::class.java)

        val sharedPref = sharedPreferences(pagMain)

        dataIntent()

        val caixaEmail = binding.boxEmail
        val caixaSenha = binding.boxSenha
        val envioEmailSenha = binding.logar

        if (sharedPref != null) {
            logar(envioEmailSenha, caixaEmail, caixaSenha, sharedPref, pagMain)
        }

        cadastrar()

        esquecerSenha()
    }

    private fun sharedPreferences(pagMain: Intent): SharedPreferences? {
        val sharedPref = getPreferences(MODE_PRIVATE)
        val logou = sharedPref.getBoolean("logou", false)

        if (logou) {
            startActivity(pagMain)
        }
        return sharedPref
    }

    private fun binding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun dataIntent() {
        val dadoEmail = intent.getStringExtra("dadoEmail")
        user.email = dadoEmail.toString()

        val dadoSenha = intent.getStringExtra("dadoSenha")
        user.password = dadoSenha.toString()

        user.name = intent.getStringExtra("dadoNome").toString()
    }

    private fun esquecerSenha() {
        binding.esqueceuSenha.setOnClickListener {
            startActivity(Intent(this, EsqueciMinhaSenhaActivity::class.java))
        }
    }

    private fun cadastrar() {
        binding.cadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    private fun logar(
        envioEmailSenha: Button,
        caixaEmail: EditText,
        caixaSenha: EditText,
        sharedPref: SharedPreferences,
        pagMain: Intent
    ) {
        envioEmailSenha.setOnClickListener {
            val emailTentado = caixaEmail.text.toString()
            val senhaTentada = caixaSenha.text.toString()

            if (user.authenticate(emailTentado, senhaTentada)) {
                sharedPref.edit().putBoolean("logou", true).apply()
                pagMain.putExtra("dadoNome", user.name)
                startActivity(pagMain)
                caixaEmail.text.clear()
                caixaSenha.text.clear()
            } else {
                Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
                caixaSenha.text.clear()
            }
        }
    }

}