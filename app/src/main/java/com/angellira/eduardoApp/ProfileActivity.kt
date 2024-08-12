package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.angellira.eduardoApp.databinding.ActivityProfileBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.launch


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var user = User()
    private val prefs by lazy { Preferences(this) }
    private val apiService = ApiServiceFaceBlog.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        setupView()
        setSupportActionBar(binding.myToolbar)
        setUser()
        deslogar()
        createDialogDelete()
    }

    private fun deleteUser() {
        val pagLogin = Intent(this, SplashActivity::class.java)
        lifecycleScope.launch {
            apiService.deleteUser(prefs.id.toString())
            prefs.clear()
            prefs.isLogged = false
            startActivity(pagLogin)
            finishAffinity()
        }
    }

    private fun createDialogDelete() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@ProfileActivity)
        builder
            .setMessage("Você tem certeza que deseja deletar todos os dados da sua conta?")
            .setTitle("Deletar dados da sua conta")
            .setPositiveButton("Deletar os dados") { dialog, wich ->
                deleteUser()
            }
            .setNegativeButton("Cancelar") { dialog, wich ->
            }

        val dialog: AlertDialog = builder.create()
        binding.delete.setOnClickListener {
            dialog.show()
        }
    }

    private fun setProfilePicture() {
        try {
            binding.pictureProfile.load(user.img)
        }
        catch (e:Exception)
        {
            user.img = "https://static.vecteezy.com/system/resources/thumbnails/005/129/844/small_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg"
            binding.pictureProfile.load(user.img)
        }
    }

    private fun setupView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private  fun setUser() {
        lifecycleScope.launch{
            user = apiService.getUser(prefs.id.toString())
            setProfilePicture()
            showDataUser()
        }
    }

    private fun deslogar(
    ) {
        val pagLogin = Intent(this, SplashActivity::class.java)
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
    ) {
        val nomeTextBox = binding.nome
        val emailTextBox = binding.email
        val senhaTextBox = binding.senha
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