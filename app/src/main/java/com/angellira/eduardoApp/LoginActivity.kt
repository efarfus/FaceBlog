package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.eduardoApp.databinding.ActivityLoginBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val user = User()
    private val prefs by lazy { Preferences(this) }
    private val apiService = ApiServiceFaceBlog.retrofitService
    private var users: MutableList<User> = mutableListOf()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        setupView()
        setSupportActionBar(binding.myToolbar)
        val pagMain = Intent(this, SplashActivity::class.java)

        val sharedPref = sharedPreferences(pagMain)

        dataIntent()

        val caixaEmail = binding.boxEmail
        val caixaSenha = binding.boxSenha
        val envioEmailSenha = binding.logar

        if (sharedPref != null)
        run {
            logar(envioEmailSenha, caixaEmail, caixaSenha, pagMain)
        }

        cadastrar()

        esquecerSenha()
    }

    private fun setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun sharedPreferences(pagMain: Intent): Preferences {
        val logou = prefs.isLogged
        if (logou) {
            startActivity(pagMain)
            finish()
        }
        return prefs
    }

    private fun binding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun dataIntent() {
        user.email = prefs.email.toString()
        user.password = prefs.password.toString()
        user.name = prefs.name.toString()
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
        pagMain: Intent
    ) {
        envioEmailSenha.setOnClickListener {
            val emailTentado = caixaEmail.text.toString()
            val senhaTentada = caixaSenha.text.toString()


            lifecycleScope.launch {
                if (checkCredentials(emailTentado, senhaTentada)) {
                    saveId(emailTentado, senhaTentada)
                    prefs.isLogged = true
                    startActivity(pagMain)
                    clear(caixaEmail, caixaSenha)
                } else {
                    Toast.makeText(this@LoginActivity, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
                    clear(caixaSenha, caixaEmail)
                }
            }
        }
    }

    private fun clear(caixaEmail: EditText, caixaSenha: EditText) {
        caixaEmail.text.clear()
        caixaSenha.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.no_icon, menu)
        return true
    }

    private suspend fun checkCredentials(email: String, password: String): Boolean {
        return if (email.isNotEmpty() && password.isNotEmpty()) {
            users = apiService.getUsers().values.toMutableList()
            users.any { it.email == email && it.password == password }
        } else {
            false
        }
    }

    private suspend fun saveId(email: String, password: String) {
        val user = apiService.getUsers().values.find { it.email == email && it.password == password }
        if (user != null) {
            val idOfThisUser = apiService.getUsers().entries.find { it.value.email == user.email }?.key
            prefs.id = idOfThisUser.toString()
        }
    }

}