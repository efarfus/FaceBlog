package com.angellira.eduardoApp

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityAddMarketItemBinding
import com.angellira.eduardoApp.model.MarketItem
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMarketItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMarketItemBinding
    private lateinit var marketItemDao: MarketItemDao
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase
    private val prefs by lazy { Preferences(this) }
    private val marketItem = MarketItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding()
        setupView()

        // Inicializa o banco de dados e DAOs antes de outras operações
        lifecycleScope.launch {
            initializeDatabase()  // Inicializa o banco de dados e DAOs
            setUser()  // Agora userDao está inicializado
            loadGif()
            addItem()
        }
    }

    private suspend fun initializeDatabase() {
        withContext(Dispatchers.IO) {
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "faceblog.db"
            ).build()
            marketItemDao = db.marketItemDao()
            userDao = db.userDao()
        }
    }

    private suspend fun setUser() {
        withContext(Dispatchers.IO) {
            marketItem.user = userDao.get(prefs.id.toString())?.name ?: "Unknown"
        }
    }

    private fun addItem() {
        binding.cadastrar.setOnClickListener {
            if (binding.boxDescricao.text.toString().isNotEmpty() &&
                binding.boxTitulo.text.toString().isNotEmpty() &&
                binding.boxPreco.text.toString().isNotEmpty() &&
                binding.boxImageSrc.text.toString().isNotEmpty()
            ) {

                marketItem.title = binding.boxTitulo.text.toString()
                marketItem.description = binding.boxDescricao.text.toString()
                marketItem.price = binding.boxPreco.text.toString()
                marketItem.img = binding.boxImageSrc.text.toString()

                lifecycleScope.launch(Dispatchers.IO) {
                    marketItemDao.insert(marketItem)
                }
                clearBoxes()
                Toast.makeText(this, "Item Cadastrado com sucesso", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MarketplaceActivity::class.java))
                finishAffinity()
            } else {
                Toast.makeText(this, "Dados estão incorretos, tente novamente", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun clearBoxes() {
        binding.boxPreco.text.clear()
        binding.boxTitulo.text.clear()
        binding.boxDescricao.text.clear()
        binding.boxImageSrc.text.clear()
    }

    private fun loadGif() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        binding.gif.load(R.drawable.marketplacegif, imageLoader)
    }

    private fun binding() {
        binding = ActivityAddMarketItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}
