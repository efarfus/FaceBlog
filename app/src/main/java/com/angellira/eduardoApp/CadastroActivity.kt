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
import androidx.room.Room
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityCadastroBinding
import com.angellira.eduardoApp.model.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.UUID.randomUUID

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private val user = User()
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        database()
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
            val teste = randomUUID().toString()
            user.id = teste
            user.name = binding.boxNome.text.toString()
            user.email = binding.boxEmail.text.toString()
            user.password = binding.boxSenha.text.toString()
            user.img = binding.boxImageSrc.text.toString()
            val passwordConfirmation = binding.boxConfirmarSenha.text.toString()

            if (user.password == passwordConfirmation && user.email.isNotEmpty() && user.name.isNotEmpty() && user.password.isNotEmpty() && passwordConfirmation.isNotEmpty()) {

                lifecycleScope.launch(IO) {
                    userDao.insert(user)
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

    private fun database() {
        lifecycleScope.launch(IO) {

            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "faceblog.db"
            ).build()
            userDao = db.userDao()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.no_icon, menu)
        return true
    }
}