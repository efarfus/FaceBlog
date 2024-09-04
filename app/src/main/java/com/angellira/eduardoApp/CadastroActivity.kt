package com.angellira.eduardoApp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityCadastroBinding
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID.randomUUID

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private val user = User()
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private val apiService = ApiServiceFaceBlog.retrofitService
    private val PICK_IMAGE_REQUEST = 1
    private var imagemBase64: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.corfundo)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.corfundo)
        binding()
        database()
        setupView()
        setSupportActionBar(binding.myToolbar)

//        autorizacao()


        val intent = Intent(this, LoginActivity::class.java)
        cadastrar(user, intent)
    }

//    private fun autorizacao() {
//        binding.boxImageSrc.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
//        }
//
//    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST
            && resultCode == Activity.RESULT_OK
            && data != null){
            val imageUri = data.data

            imagemBase64 = encodeImageToBase64(imageUri!!)
        }
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

                if (user.img == "") {
                    user.img = "https://static.vecteezy.com/system/resources/thumbnails/005/129/844/small_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg"
                }
                lifecycleScope.launch(IO) {
                    try{
                        apiService.saveUser(user)
                    }catch (e:Exception) {
                        withContext(Main){
                            Toast.makeText(this@CadastroActivity, "Não é possível cadastrar sem internet, conecte-se e tente novamente", Toast.LENGTH_LONG).show()
                            binding.boxSenha.text.clear()
                            binding.boxNome.text.clear()
                            binding.boxEmail.text.clear()
                            binding.boxConfirmarSenha.text.clear()
                        }
                        return@launch
                    }
                    userDao.insert(user)
                    withContext(Main){
                        startActivity(intent)
                        finishAffinity()
                    }
                }

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