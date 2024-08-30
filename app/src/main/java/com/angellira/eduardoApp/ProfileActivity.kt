package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import coil.load
import com.angellira.eduardoApp.adapter.PostAdapter
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.PostsDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityProfileBinding
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.network.PexelsApi
import com.angellira.eduardoApp.preferences.Preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var user = User()
    private val pexelsApi = PexelsApi()
    private val prefs by lazy { Preferences(this) }
    private lateinit var db: AppDatabase
    private lateinit var postsDao: PostsDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var marketItemDao: MarketItemDao
    private lateinit var userDao: UserDao
    private val apiService = ApiServiceFaceBlog.retrofitService
    private val PICK_IMAGE_REQUEST = 1
    private var imagemBase64: String? = null

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
        createDialogEdit()
        cancelEdit()
        showInfos()
        createPhoto()

    }

    private fun loadPosts() {
        lifecycleScope.launch(IO) {
            try {
                val postsList = apiService.getUserPosts(user.name)
                withContext(Main) {
                    recyclerView(postsList.reversed())
                }
            } catch (e: Exception) {
                val postsList = postsDao.getPostsUser(user.name)
                withContext(Main) {
                    recyclerView(postsList.reversed())
                }
                withContext(Main) {
                    binding.error.visibility = VISIBLE
                }
            }

        }
    }

    fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }


    private fun recyclerView(listPosts: List<Posts>) {
        recyclerView = binding.recyclerViewPosts
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PostAdapter(listPosts) { _, _, id, _ ->
            prefs.idPost = id
            val intent = Intent(this@ProfileActivity, DetailedPostActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun showInfos() {
        binding.showInfos.setOnClickListener {
            binding.nome.visibility = VISIBLE
            binding.email.visibility = VISIBLE
            binding.senha.visibility = VISIBLE
            binding.showInfos.visibility = GONE
            binding.cancelEditButton.visibility = VISIBLE
            binding.recyclerViewPosts.visibility = GONE
        }
    }

    private fun createPhoto() {
        binding.pictureProfile.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Deseja gerar uma foto aleatória para foto de perfil?")
                .setMessage("Foto gerada através de API Pexels.")
                .setPositiveButton("Sim") { _, _ ->
                    lifecycleScope.launch(IO) {
                        user.img = pexelsApi.getRandomPhotoUrl().toString()
                        apiService.putUser(prefs.id.toString(), user)
                        userDao.putImg(user.img, prefs.id.toString())
                        withContext(Main) {
                            setProfilePicture()
                        }
                    }
                }
                .setNeutralButton("Colocar imagem própria") { _, _ ->
                    startActivity(Intent(this, UrlSetActivity::class.java))
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun cancelEdit() {
        binding.cancelEditButton.setOnClickListener {
            switchLayoutBack()
        }
    }

    private fun confirmEdit() {

        catchInfos()
        putAll()
        startActivity(Intent(this, LoginActivity::class.java))
        prefs.clear()
    }


    private fun edit() {
        binding.editarPerfil.setOnClickListener {
            switchLayout()
        }
    }

    private fun putAll() {

//        user.img = decodeBase64ToBitmap(imagemBase64!!).toString()
        lifecycleScope.launch(IO) {
            try {
                apiService.putUser(prefs.id.toString(), user)
            } catch (e: Exception) {
                withContext(Main) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Não é possível alterar algo sem internet, conecte-se e tente novamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return@launch
            }
            userDao.updateUser(prefs.id.toString(), user.name, user.email, user.password)
        }
    }

    private fun catchInfos() {
        if (binding.editTextName.text.isNotEmpty() && binding.editTextEmailAddress.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty()){
            user.name = binding.editTextName.text.toString()
            user.email = binding.editTextEmailAddress.text.toString()
            user.password = binding.editTextPassword.text.toString()
        }
        else{
            binding.editTextName.text.clear()
            binding.editTextEmailAddress.text.clear()
            binding.editTextPassword.text.clear()
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun switchLayout() {
        binding.editTextName.visibility = VISIBLE
        binding.editTextEmailAddress.visibility = VISIBLE
        binding.editTextPassword.visibility = VISIBLE
        binding.cancelEditButton.visibility = VISIBLE
        binding.confirmButton.visibility = VISIBLE
        binding.recyclerViewPosts.visibility = GONE
        binding.email.visibility = GONE
        binding.senha.visibility = GONE
        binding.showInfos.visibility = GONE
    }


    private fun switchLayoutBack() {
        binding.editTextName.visibility = GONE
        binding.editTextEmailAddress.visibility = GONE
        binding.editTextPassword.visibility = GONE
        binding.cancelEditButton.visibility = GONE
        binding.confirmButton.visibility = GONE
        binding.showInfos.visibility = VISIBLE
        binding.email.visibility = GONE
        binding.senha.visibility = GONE
        binding.recyclerViewPosts.visibility = VISIBLE

    }

    private fun deleteUser() {
        val pagLogin = Intent(this, SplashActivity::class.java)
        lifecycleScope.launch(IO) {
            try {
                apiService.deleteUser(prefs.id.toString())
            } catch (e: Exception) {
                withContext(Main) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Não é possível deletar sem internet, conecte-se e tente novamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return@launch
            }
            userDao.delete(user)

            withContext(Main) {
                prefs.clear()
                prefs.isLogged = false
                startActivity(pagLogin)
                finishAffinity()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                imagemBase64 = encodeImageToBase64(imageUri)


                lifecycleScope.launch(IO) {
                    putAll()
                    withContext(Main) {
                        setProfilePicture()
                    }
                }
            }
        }
    }

    private fun autorizacao() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    fun encodeImageToBase64(imageUri: Uri): String? {
        val imageStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(imageStream)

        if (bitmap == null) {
            return null
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
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

    private fun createDialogEdit() {
        binding.confirmButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Você tem certeza de que deseja editar sua conta?")
                .setMessage("Após a edição os dados serão alterados na sua conta.")
                .setPositiveButton("Sim") { _, _ ->
                    confirmEdit()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun createDialogDelete() {
        binding.delete.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Você tem certeza de que deseja excluir sua conta?")
                .setMessage("Após a exclusão não será possível a recuperação de nenhum dado salvo na sua conta.")
                .setPositiveButton("Sim") { _, _ ->
                    deleteUser()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setProfilePicture() {

        binding.pictureProfile.load(user.img)

        //        try {
//            val image = decodeBase64ToBitmap(user.img)
//            binding.pictureProfile.setImageBitmap(image)
//        }catch (e:Exception){
//            binding.pictureProfile.load(user.img)
//        }

    }


    private fun setProfilePictureAlterar() {
        try {
//            binding.pictureProfile.setImageBitmap()
        } catch (e: Exception) {
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

    private fun setUser() {
        lifecycleScope.launch(IO) {

            try {
                user = apiService.getUserById(prefs.id.toString())
                withContext(Main) {
                    setProfilePicture()
                    showDataUser()
                    loadPosts()
                }
            } catch (e: Exception) {
                user = userDao.get(prefs.id.toString())!!
                withContext(Main) {
                    setProfilePicture()
                    showDataUser()
                }
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
        setSupportActionBar(binding.myToolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }
}