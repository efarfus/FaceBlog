package com.angellira.eduardoApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        setSupportActionBar(binding.myToolbar)
        initializeDatabase()
        loadData()
        createDialogDelete()
        closeDelete()
    }

    private fun ifOwner() {
        if (marketItem?.user  == user?.name) {
            binding.moreOptions.visibility = VISIBLE
            binding.moreOptions.setOnClickListener {
                binding.deleteButton.visibility = VISIBLE
            }
        }
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

    private fun closeDelete() {
        binding.main.setOnClickListener {
            binding.deleteButton.visibility = INVISIBLE
        }
    }

    private fun createDialogDelete() {
        binding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Você tem certeza de que deseja excluir esse item?")
                .setMessage("Após a exclusão não será possível a recuperação de nenhum dado salvo.")
                .setPositiveButton("Sim") { _, _ ->
                    deletePost()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun deletePost() {
        lifecycleScope.launch(IO) {
            apiService.deleteItem(prefs.idItem.toString())
            marketItemDao.delete(marketItem!!)
            withContext(Main) {
                binding.deleteButton.visibility = INVISIBLE
                startActivity(Intent(this@DetailedItemActivity, MainActivity::class.java))
            }
        }
    }

    private fun loadData() {
        lifecycleScope.launch(IO) {
            try {
                user = apiService.getUserById(prefs.id.toString())
                marketItem = apiService.getItemById(prefs.idItem.toString())
            }catch (e:Exception){
                user = userDao.get(prefs.id.toString())!!
                marketItem = marketItemDao.get(prefs.idItem.toString())!!
            }


            withContext(Main) {
                if (user != null && marketItem != null) {
                    binding.imageItem.load(marketItem!!.img)
                    binding.nameUser.text = user!!.name
                    binding.priceItem.text = marketItem!!.price
                    binding.itemDescription.text = marketItem!!.description
                    binding.itemTitle.text = marketItem!!.title
                    binding.imageUser.load(user!!.img)

                } else {
                    Toast.makeText(
                        this@DetailedItemActivity,
                        "Dados não encontrados",
                        Toast.LENGTH_LONG
                    ).show()
                }

                ifOwner()
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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_profile -> {
            startActivity(Intent(this, ProfileActivity::class.java))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }
}