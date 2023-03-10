package com.example.favoritos.Adapter


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.favoritos.Activity.CadastroActivity
import com.example.favoritos.ViewModel.CrudViewModel
import com.example.favoritos.ViewModel.FilmeModel
import com.example.favoritos.R

class AdapterFilmes(private val dataSet: MutableList<FilmeModel>?) :
    Adapter<AdapterFilmes.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView
        val avaliacao: TextView
        val status: TextView
        val capa: ImageView
        val btnDelete: Button
        val btnAtualizar: Button
        val crudViewModel: CrudViewModel

        init {
            nome = view.findViewById(R.id.txtNome)
            avaliacao = view.findViewById(R.id.txtavaliacao)
            capa = view.findViewById(R.id.imgCapa)
            btnDelete = view.findViewById(R.id.btnDelete)
            btnAtualizar = view.findViewById(R.id.btnEditar)
            crudViewModel = CrudViewModel(view.context)
            status = view.findViewById(R.id.txtStatus)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.card, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context


        dataSet?.let {
            val bmp = it[position].capa?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            viewHolder.capa.setImageBitmap(bmp?.let {
                Bitmap.createScaledBitmap(
                    it,
                    150,
                    200,
                    false
                )
            })
            viewHolder.nome.text = "FilmeModel: ${it[position].nome}"
            viewHolder.status.text = "Status: ${it[position].status}"
            it[position].avaliacao?.let {
                viewHolder.avaliacao.text = "Avalia????o: $it"
            }
            viewHolder.btnDelete.setOnClickListener { view ->
                view
                viewHolder.crudViewModel.delete(it[position])
                it.removeAt(position)
                notifyItemRemoved(position)
            }
            viewHolder.btnAtualizar.setOnClickListener { view ->
                view

                val intent: Intent = Intent(view.context, CadastroActivity::class.java).putExtra(
                    "uid",
                    it[position].uid
                )
                view.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = dataSet?.size ?: 0


}
