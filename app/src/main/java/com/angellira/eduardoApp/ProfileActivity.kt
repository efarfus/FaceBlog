package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityProfileBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.preferences.Preferences


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val user = User()
    private val prefs by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        setupView()
        setSupportActionBar(binding.myToolbar)


        val pagLogin = Intent(this, SplashActivity::class.java)


        user.name = prefs.name.toString()
        val nomeTextBox = binding.nome
        user.email = prefs.email.toString()
        val emailTextBox = binding.email
        user.password = prefs.password.toString()
        val senhaTextBox = binding.senha

        showDataUser(nomeTextBox, emailTextBox, senhaTextBox)
        deslogar(pagLogin)

    }

    private fun setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun deslogar(
        pagLogin: Intent
    ) {
        val buttonLogOff = binding.deslogar
        buttonLogOff.setOnClickListener {
            prefs.clear()
            prefs.isLogged = false
            startActivity(pagLogin)
            finishAffinity()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDataUser(
        nomeTextBox: TextView,
        emailTextBox: TextView,
        senhaTextBox: TextView
    ) {
        nomeTextBox.setText("Nome: ${user.name}")
        emailTextBox.setText("Email: ${user.email}")
        senhaTextBox.setText("Senha: ${user.password}")
    }

    private fun binding() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }

}