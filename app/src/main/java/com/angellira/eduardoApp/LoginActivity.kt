package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.room.Room
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.PostsDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityLoginBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val user = User()
    private val prefs by lazy { Preferences(this) }
    private var users: MutableList<User> = mutableListOf()
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private val apiService = ApiServiceFaceBlog.retrofitService


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

        database()

        val caixaEmail = binding.boxEmail
        val caixaSenha = binding.boxSenha
        val envioEmailSenha = binding.logar

        if (sharedPref != null)
        {
            run {
                logar(envioEmailSenha, caixaEmail, caixaSenha, pagMain)
            }
        }


        cadastrar()

        esquecerSenha()
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


            lifecycleScope.launch(IO) {
                if (checkCredentials(emailTentado, senhaTentada)) {
                    saveId(emailTentado, senhaTentada)
                    withContext(Main){
                        prefs.isLogged = true
                        startActivity(pagMain)
                        clear(caixaEmail, caixaSenha)
                    }
                } else {
                    withContext(Main){
                        Toast.makeText(
                            this@LoginActivity,
                            "Email ou senha incorretos",
                            Toast.LENGTH_LONG
                        ).show()
                        clear(caixaSenha, caixaEmail)
                    }
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
            try {
                users = apiService.getUsers().toMutableList()
                return users.any { it.email == email && it.password == password }
            }catch (e:Exception){
                users = userDao.getAll().toMutableList()
                return users.any { it.email == email && it.password == password }
            }

        } else {
            false
        }
    }

    private suspend fun saveId(email: String, password: String) {
        lifecycleScope.launch(IO) {
            try {
                val userId = apiService.getUserByEmailAndPassword(email, password).id
                prefs.id = userId
            }catch (e:Exception){
                val userId = userDao.getUserByEmailAndPassword(email, password)?.id.toString()
                prefs.id = userId
            }
        }
    }
}