package com.angellira.eduardoApp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angellira.eduardoApp.adapter.PostAdapter
import com.angellira.eduardoApp.databinding.ActivityMainBinding
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User
import com.angellira.eduardoApp.network.ApiServiceFaceBlog
import com.angellira.eduardoApp.preferences.Preferences
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private var user = User()
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val prefs by lazy { Preferences(this) }
    private val postList = listOf(
        Posts("1", "Ponte Preta","Experimentei uma nova receita hoje e ficou incrível! Fiz um risoto de camarão com limão siciliano que ficou de lamber os dedos. Se alguém quiser a receita, só pedir nos comentários!  #Gastronomia #CozinhandoEmCasa #Receitas #Delícia #Risoto", "https://firebasestorage.googleapis.com/v0/b/pets-f26d1.appspot.com/o/pastor-alemao-filhote.png?alt=media&token=ed8ab0d9-3d4d-466a-b937-e14f7d481886"),
        Posts("2","XXXJosué", "Experimentei uma nova receita hoje e ficou incrível! Fiz um risoto de camarão com limão siciliano que ficou de lamber os dedos. Se alguém quiser a receita, só pedir nos comentários!  #Gastronomia #CozinhandoEmCasa #Receitas #Delícia #Risoto", "https://recreio.com.br/media/uploads/animacoes/luffy_one_piece_capa.jpg"),
        Posts("3","Eduardo Farfus", "Acabei de explorar uma nova trilha de montanha e a vista era simplesmente deslumbrante! \uD83C\uDF04✨ A natureza nunca deixa de me surpreender. Aproveitei para tirar algumas fotos incríveis. Quem quiser dicas de trilhas pela região, só mandar mensagem!  #Natureza #Aventura #Trilhas #Exploração #VidaAoArLivre", "https://img.freepik.com/psd-gratuitas/ilustracao-3d-de-avatar-ou-perfil-humano_23-2150671122.jpg"),
        Posts("4","Bruno Da Costa Silva", "Hoje foi dia de cuidar do jardim! Plantei algumas flores novas e estou animada para ver como vão ficar na primavera. Nada como colocar as mãos na terra e sentir a energia da natureza. Alguém mais aqui adora jardinagem?  #Jardinagem #Flores #Primavera #Natureza #Hobby", "https://upload.wikimedia.org/wikipedia/pt/9/96/Capa-AM_%28oficial%29.jpeg"),
        Posts("5","Cecilia de Moraes", "Hoje foi dia de cuidar do jardim! Plantei algumas flores novas e estou animada para ver como vão ficar na primavera. Nada como colocar as mãos na terra e sentir a energia da natureza. Alguém mais aqui adora jardinagem?  #Jardinagem #Flores #Primavera #Natureza #Hobby", "https://wallpapers.com/images/hd/vinland-saga-minimalist-thorfinn-illustration-h1zhyk3bicbd2q1v.jpg"),
        Posts("6","Dionisio HalfBlood", "Hoje foi dia de cuidar do jardim! Plantei algumas flores novas e estou animada para ver como vão ficar na primavera. Nada como colocar as mãos na terra e sentir a energia da natureza. Alguém mais aqui adora jardinagem?  #Jardinagem #Flores #Primavera #Natureza #Hobby", "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Malphite_1.jpg")

    )
    private val apiService = ApiServiceFaceBlog.retrofitService


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setSupportActionBar(binding.myToolbar)
        hint()
        marketplace(Intent(this, MarketplaceActivity::class.java))
        recyclerView()
        postar()
    }

    private fun postar() {
        binding.enviarPost.setOnClickListener {
            lifecycleScope.launch {
                setUser()
                val randomId = UUID.randomUUID().toString()
                val post = Posts(randomId, "user", binding.caixaPost.text.toString(), "")
                apiService.savePost(post)
            }
        }
    }

    private suspend fun setUser() {
        user = apiService.getUser("1")
    }

    private fun recyclerView() {
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

    private fun hint() {
        user.name = prefs.name.toString()
        binding.caixaPost.hint = "No que você está pensando, ${user.name}?"
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