package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.angellira.eduardoApp.databinding.ActivityCadastroBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.launch
import java.util.UUID

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private val prefs by lazy { Preferences(this) }
    private val apiService = ApiServiceFaceBlog.retrofitService
    private val user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        setupView()
        setSupportActionBar(binding.myToolbar)

        val intent = Intent(this, LoginActivity::class.java)

        cadastrar(user, intent)
    }

    private fun setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
            user.id = UUID.randomUUID().toString()
            user.img = binding.boxImageSrc.text.toString()
            val passwordConfirmation = binding.boxConfirmarSenha.text.toString()

            if (user.password == passwordConfirmation && user.email.isNotEmpty() && user.name.isNotEmpty() && user.password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {

                prefs.id = user.id
                lifecycleScope.launch {
                    apiService.saveUser(user)
                }
                startActivity(intent)
                finishAffinity()
            } else {
                Toast.makeText(this, "Dados estão incorretos, tente novamente", Toast.LENGTH_LONG)
                    .show()
                binding.boxSenha.text.clear()
                binding.boxConfirmarSenha.text.clear()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.no_icon, menu)
        return true
    }
}