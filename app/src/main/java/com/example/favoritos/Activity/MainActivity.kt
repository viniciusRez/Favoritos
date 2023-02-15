package com.example.favoritos.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.favoritos.Adapter.AdapterFilmes
import com.example.favoritos.Service.AppDataBase
import com.example.favoritos.ViewModel.FilmeModel
import com.example.favoritos.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appdb: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appdb = AppDataBase.getDatabase(this)
        val toolbar: Toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = "Filmes cadastrados"
        setSupportActionBar(toolbar)
        val btnCadastra:Button = findViewById(R.id.btnCadastra)
        btnCadastra.setOnClickListener {

            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFilmes)
        recyclerView.adapter = AdapterFilmes(
            dataSet = readData()
        )
    }
    private fun readData(): MutableList<FilmeModel> {
        lateinit var filmeModel: FilmeModel
        GlobalScope.launch {
            val data: MutableList<FilmeModel> = appdb.filmeDao().getAll() as MutableList<FilmeModel>
        }
        val data: MutableList<FilmeModel> = appdb.filmeDao().getAll() as MutableList<FilmeModel>
        return data
    }
}