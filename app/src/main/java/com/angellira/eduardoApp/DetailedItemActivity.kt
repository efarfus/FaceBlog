package com.angellira.eduardoApp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.load
import com.angellira.eduardoApp.database.AppDatabase
import com.angellira.eduardoApp.database.dao.MarketItemDao
import com.angellira.eduardoApp.database.dao.UserDao
import com.angellira.eduardoApp.databinding.ActivityDetailedItemBinding
import com.angellira.eduardoApp.model.MarketItem
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedItemBinding
    private val prefs by lazy { Preferences(this) }
    private lateinit var marketItemDao: MarketItemDao
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase
    private var marketItem: MarketItem? = null
    private var user: User? = null
    private val apiService = ApiServiceFaceBlog.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding()
        setupView()
        initializeDatabase()  // Certifique-se de que o banco de dados e DAOs estão prontos
        loadData()  // Carrega dados com segurança
    }

    private fun initializeDatabase() {
        lifecycleScope.launch(IO) {
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "faceblog.db"
            ).build()
            marketItemDao = db.marketItemDao()
            userDao = db.userDao()
        }
    }

    private fun loadData() {
        lifecycleScope.launch(IO) {
            try {
                user = apiService.getUserById(prefs.id.toString())
                marketItem = apiService.getItemById(prefs.idItem.toString())
            }catch (e:Exception){
                user = userDao.get(prefs.id.toString())
                marketItem = marketItemDao.get(prefs.idItem.toString())
            }


            withContext(Main) {
                if (user != null && marketItem != null) {
                    // Atualize a UI apenas se user e marketItem não forem null
                    binding.imageItem.load(marketItem?.img)
                    binding.nameUser.text = user?.name
                    binding.priceItem.text = marketItem?.price
                    binding.itemDescription.text = marketItem?.description
                    binding.itemTitle.text = marketItem?.title
                    binding.imageUser.load(user?.img)

                } else {
                    // Lidar com o caso em que user ou marketItem são null
                    Toast.makeText(
                        this@DetailedItemActivity,
                        "Dados não encontrados",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun binding() {
        binding = ActivityDetailedItemBinding.inflate(layoutInflater)
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
}