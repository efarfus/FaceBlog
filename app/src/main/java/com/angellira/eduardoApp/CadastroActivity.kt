package com.angellira.eduardoApp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angellira.eduardoApp.databinding.ActivityCadastroBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.preferences.Preferences

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private val prefs by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val user = User()
        val intent = Intent(this, LoginActivity::class.java)


        cadastrar(user, intent)
    }

    private fun binding() {
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun cadastrar(
        user: User,
        intent: Intent
    ) {
        binding.cadastrar.setOnClickListener {
            user.name = binding.boxNome.text.toString()
            user.email = binding.boxEmail.text.toString()
            user.password = binding.boxSenha.text.toString()
            user.passwordConfirmation = binding.boxConfirmarSenha.text.toString()

            if (user.password == user.passwordConfirmation && user.email != "" && user.name != "") {
                prefs.name = user.name
                prefs.email = user.email
                prefs.password = user.password
                startActivity(intent)
                finishAffinity()
            } else {
                Toast.makeText(this, "Dados estão incorretos, tente novamente", Toast.LENGTH_LONG).show()
                binding.boxSenha.text.clear()
                binding.boxConfirmarSenha.text.clear()
            }
        }
    }
}