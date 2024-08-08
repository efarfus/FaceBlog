package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.eduardoApp.databinding.ActivityMainBinding
import com.angellira.eduardoApp.adapter.Post
import com.angellira.eduardoApp.adapter.PostAdapter
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.preferences.Preferences

class MainActivity : AppCompatActivity() {
    private val user = User()
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val prefs by lazy { Preferences(this) }
    private val postList = listOf(
        Post("XXXJosué", "Experimentei uma nova receita hoje e ficou incrível! Fiz um risoto de camarão com limão siciliano que ficou de lamber os dedos. Se alguém quiser a receita, só pedir nos comentários!  #Gastronomia #CozinhandoEmCasa #Receitas #Delícia #Risoto", R.drawable.avatar1),
        Post("Eduardo Farfus", "Acabei de explorar uma nova trilha de montanha e a vista era simplesmente deslumbrante! \uD83C\uDF04✨ A natureza nunca deixa de me surpreender. Aproveitei para tirar algumas fotos incríveis. Quem quiser dicas de trilhas pela região, só mandar mensagem!  #Natureza #Aventura #Trilhas #Exploração #VidaAoArLivre", R.drawable.avatar2),
        Post("Bruno Da Costa Silva", "Hoje foi dia de cuidar do jardim! Plantei algumas flores novas e estou animada para ver como vão ficar na primavera. Nada como colocar as mãos na terra e sentir a energia da natureza. Alguém mais aqui adora jardinagem?  #Jardinagem #Flores #Primavera #Natureza #Hobby", R.drawable.avatar3),
        Post("Cecilia de Moraes", "Hoje foi dia de cuidar do jardim! Plantei algumas flores novas e estou animada para ver como vão ficar na primavera. Nada como colocar as mãos na terra e sentir a energia da natureza. Alguém mais aqui adora jardinagem?  #Jardinagem #Flores #Primavera #Natureza #Hobby", R.drawable.avatar1),
        Post("Dionisio HalfBlood", "Hoje foi dia de cuidar do jardim! Plantei algumas flores novas e estou animada para ver como vão ficar na primavera. Nada como colocar as mãos na terra e sentir a energia da natureza. Alguém mais aqui adora jardinagem?  #Jardinagem #Flores #Primavera #Natureza #Hobby", R.drawable.avatar4)
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setSupportActionBar(binding.myToolbar)

        user.name = prefs.name.toString()
        val bemVindoBox = binding.bemVindo

        val marketplaceActivity = Intent(this, MarketplaceActivity::class.java)

        mensagem(bemVindoBox)
        marketplace(marketplaceActivity)

        recyclerView = binding.recyclerViewPosts
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PostAdapter(postList)
        recyclerView.adapter = adapter
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mensagem(bemVindoBox: TextView) {
        bemVindoBox.setText("Bem vindo, ${user.name}!")
    }



    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        R.id.action_profile -> {
            startActivity(Intent(this,ProfileActivity::class.java))

            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun marketplace(marketplaceActivity: Intent) {
        binding.options.setOnClickListener {
            startActivity(marketplaceActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.itens, menu)
        return true
    }

}