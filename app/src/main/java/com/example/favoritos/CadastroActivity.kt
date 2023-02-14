package com.example.favoritos

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.ByteArrayOutputStream


class CadastroActivity : AppCompatActivity() {


    private lateinit var appdb: AppDataBase
    private var resultLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data:Intent? = result.data
            if(data != null){
                val uri = data.data
                val imageView: ImageView = findViewById(R.id.imgCapa)
                imageView.setBackgroundResource(androidx.appcompat.R.color.secondary_text_default_material_dark)
                imageView.setImageURI(uri)
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        appdb = AppDataBase.getDatabase(this)
        //EditText
        val edtNome: EditText = findViewById(R.id.txtNome)
        val edtDuracao: EditText = findViewById(R.id.txtDuracao)
        val edtAvaliacao: EditText = findViewById(R.id.txtAvaliacao)
        //textView
        val txtAvaliacao: TextView = findViewById(R.id.lblAvaliacao)
        //imageView
        val imgCapa: ImageView = findViewById(R.id.imgCapa)
        //buttons
        val btnCadastro: Button = findViewById(R.id.btnCadastrar)
        val btnLoadImage: Button = findViewById(R.id.btnLoadImage)
        //spinner
        val spinStatus: Spinner = findViewById(R.id.spinner)
        spinStatus.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            Status.values()
        )
        val toolbar: Toolbar = findViewById(R.id.toolbarPrincipal)
        toolbar.title = "Cadastrar"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val crudDados = CrudDados(this)
        val uidUser: Int = intent.getIntExtra("uid", -1)

        if (uidUser != -1) {
            val dadosUser = crudDados.readData(uidUser)
            val bmp = dadosUser[0].capa?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            imgCapa.setImageBitmap(bmp?.let { Bitmap.createScaledBitmap(it, 150, 200, false) })

            btnCadastro.text = "Editar"
            toolbar.title = "Editar"

            edtNome.setText(dadosUser[0].nome)
            edtDuracao.setText(dadosUser[0].duracao)
            Status.values().forEachIndexed { index, it ->
                if (it.toString() == dadosUser[0].status) {
                    spinStatus.setSelection(index)
                }
            }
            if (dadosUser[0].status == Status.ASSISTIDO.toString()) {
                dadosUser[0].avaliacao?.let {
                    edtAvaliacao.visibility = View.VISIBLE
                    txtAvaliacao.visibility = View.VISIBLE
                    edtAvaliacao.setText(it.toString())
                }
            }
            btnCadastro.setOnClickListener {
                crudDados.delete(dadosUser[0])
                newFilme()?.let { it1 ->
                    if (crudDados.writeData(it1)) {
                        Toast.makeText(this, "Filme Atualizado com sucesso", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Erro entre em contato com o suporte",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        } else {
            btnCadastro.setOnClickListener {

                newFilme()?.let { it1 ->
                    if (crudDados.writeData(it1)) {
                        Toast.makeText(this, "Filme cadastrado com sucesso", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Filme já cadastrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        btnLoadImage.setOnClickListener {
            showImage()
        }
    }

    private fun newFilme(): Filme? {
        //EditText
        val edtNome: EditText = findViewById(R.id.txtNome)
        val edtDuracao: EditText = findViewById(R.id.txtDuracao)
        val edtAvaliacao: EditText = findViewById(R.id.txtAvaliacao)
        //textView
        val txtAvaliacao: TextView = findViewById(R.id.lblAvaliacao)
        //imageView
        val imgCapa: ImageView = findViewById(R.id.imgCapa)
        //spinner
        val spinStatus: Spinner = findViewById(R.id.spinner)
        //drawable to biteArray
        val bitmap = imgCapa.drawable?.let { (it as BitmapDrawable).bitmap }
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInByte = baos.toByteArray()

        val nome: String = edtNome.text.toString()
        val duracao: String = edtDuracao.text.toString()
        val status: String = spinStatus.selectedItem.toString()
        val avaliacao: Double?

        if (nome.isNotEmpty() && duracao.isNotEmpty() && imgCapa.drawable != null) {
            if (spinStatus.selectedItem === Status.ASSISTIDO && txtAvaliacao.visibility == View.GONE) {
                Toast.makeText(this, "Avalie o filme!", Toast.LENGTH_SHORT).show()
                edtAvaliacao.visibility = View.VISIBLE
                txtAvaliacao.visibility = View.VISIBLE
                val avaliacaoString: String = edtAvaliacao.text.toString()

                return if (avaliacaoString.isNotEmpty()) {
                    val avaliacaoDouble = avaliacaoString.toDouble()
                    Filme(null, nome, duracao, avaliacaoDouble, status, imageInByte)

                } else {
                    Toast.makeText(this, "De uma nota!", Toast.LENGTH_SHORT).show()
                    null
                }
            } else {
                return Filme(null, nome, duracao, null, status, imageInByte)

            }
        } else {
            Toast.makeText(this, "Complete os campos!", Toast.LENGTH_SHORT).show()
        }
        return null
    }


    private fun showImage() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLaucher.launch(intent)

    }
}
