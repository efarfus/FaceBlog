package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.load
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.PostsDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityProfileBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.preferences.Preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var user = User()
    private val prefs by lazy { Preferences(this) }
    private lateinit var db: AppDatabase
    private lateinit var postsDao: PostsDao
    private lateinit var marketItemDao: MarketItemDao
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding()
        database()
        setupView()
        setUser()
        deslogar()
        createDialogDelete()
        edit()
        confirmEdit() //verificar o put atualizar pagina apos o put
        cancelEdit()
    }

    private fun cancelEdit() {
        binding.cancelEditButton.setOnClickListener {
            switchLayoutBack()
        }
    }

    private fun confirmEdit() {
        binding.confirmButton.setOnClickListener {
            catchInfos()
            putAll()
            switchLayoutBack()
        }
    }

    private fun edit() {
        binding.editarPerfil.setOnClickListener {
            switchLayout()
        }
    }

    private fun putAll() {
        lifecycleScope.launch(IO) {
            userDao.putName(user.name, prefs.id.toString())
            userDao.putEmail(user.email, prefs.id.toString())
            userDao.putName(user.password, prefs.id.toString())
        }
    }

    private fun catchInfos() {
        user.name = binding.editTextName.text.toString()
        user.email = binding.editTextEmailAddress.text.toString()
        user.password = binding.editTextPassword.text.toString()
    }

    private fun switchLayout() {
        binding.editTextName.visibility = VISIBLE
        binding.editTextEmailAddress.visibility = VISIBLE
        binding.editTextPassword.visibility = VISIBLE
        binding.cancelEditButton.visibility = VISIBLE
        binding.confirmButton.visibility = VISIBLE
        binding.nome.visibility = INVISIBLE
        binding.email.visibility = INVISIBLE
        binding.senha.visibility = INVISIBLE
    }

    private fun switchLayoutBack() {
        binding.editTextName.visibility = INVISIBLE
        binding.editTextEmailAddress.visibility = INVISIBLE
        binding.editTextPassword.visibility = INVISIBLE
        binding.cancelEditButton.visibility = INVISIBLE
        binding.confirmButton.visibility = INVISIBLE

        binding.nome.visibility = VISIBLE
        binding.email.visibility = VISIBLE
        binding.senha.visibility = VISIBLE
    }

    private fun deleteUser() {
        val pagLogin = Intent(this, SplashActivity::class.java)
        lifecycleScope.launch(IO) {
            userDao.delete(user)
            withContext(Main){
                prefs.clear()
                prefs.isLogged = false
                startActivity(pagLogin)
                finishAffinity()
            }
        }
    }

    private fun database() {
        lifecycleScope.launch(IO) {

            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "faceblog.db"
            ).build()
            postsDao = db.postsDao()
            marketItemDao = db.marketItemDao()
            userDao = db.userDao()
        }
    }

    private fun createDialogDelete() {
        binding.delete.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Você tem certeza de que deseja excluir sua conta?")
                .setMessage("Após a exclusão não será possível a recuperação de nenhum dado salvo na sua conta.")
                .setPositiveButton("Sim") { _,_  ->
                    deleteUser()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setProfilePicture() {

        if(user.img != "")
        {
            binding.pictureProfile.load(user.img)
        }

        else
        {
            user.img = "https://static.vecteezy.com/system/resources/thumbnails/005/129/844/small_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg"
            binding.pictureProfile.load(user.img)
        }
    }

    private fun setupView() {
        setSupportActionBar(binding.myToolbar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private  fun setUser() {
        lifecycleScope.launch(IO){
            user = userDao.get(prefs.id.toString())!!
            withContext(Main){
                setProfilePicture()
                showDataUser()
            }
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
        nomeTextBox.text = "Nome: ${user.name}"
        emailTextBox.text = "Email: ${user.email}"
        senhaTextBox.text = "Senha: ${user.password}"
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