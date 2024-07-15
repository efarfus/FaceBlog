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

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding()

        val user = User()
        val intent = Intent(this, LoginActivity::class.java)
        val sharedPreferences = getSharedPreferences("user",Context.MODE_PRIVATE)

        cadastrar(user, intent, sharedPreferences)

    }

    private fun binding() {
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun cadastrar(
        user: User,
        intent: Intent,
        sharedPreferences: SharedPreferences
    ) {
        binding.cadastrar.setOnClickListener {
            user.name = binding.boxNome.text.toString()
            user.email = binding.boxEmail.text.toString()
            user.password = binding.boxSenha.text.toString()
            user.passwordConfirmation = binding.boxConfirmarSenha.text.toString()

            if (user.password == user.passwordConfirmation && user.email != "" && user.name != "") {
                intent.putExtra("dadoNome", user.name)
                intent.putExtra("dadoEmail", user.email)
                intent.putExtra("dadoSenha", user.password)
                with(sharedPreferences.edit()) {
                    putString("name", user.name)
                    putString("email", user.email)
                    putString("senha", user.password)
                    apply()
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Senhas não coincidem", Toast.LENGTH_LONG).show()
                binding.boxSenha.text.clear()
                binding.boxConfirmarSenha.text.clear()
            }
        }
    }
}