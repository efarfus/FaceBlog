package com.angellira.eduardoApp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityProfileBinding
import com.angellira.eduardoApp.model.User



class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pagLogin = Intent(this, LoginActivity::class.java)


        binding()
        val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        user.name = sharedPreferences.getString("name", null).toString()
        val nomeTextBox = binding.nome
        user.email = sharedPreferences.getString("email", null).toString()
        val emailTextBox = binding.email
        user.password = sharedPreferences.getString("senha", null).toString()
        val senhaTextBox = binding.senha

        showDataUser(nomeTextBox, emailTextBox, senhaTextBox)
        val buttonLogOff = binding.deslogar
        buttonLogOff.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.clear().apply()
            editor.putBoolean("logou", false).apply()
            startActivity(pagLogin)
            finish()
        }

    }

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
}