package com.pjff.videogamesdb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pjff.videogamesdb.R
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.GameElementBinding

//Le pasamos un parametro al Gameadapter y le pasamos una lambda
class GameAdapter(private  val onGameClick: (GameEntity) -> Unit): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    //Aqui estan los juegos que usara mi adapter
    private var games: List<GameEntity> = emptyList()

    //el binding  va hacia el GameElementBinding ,Son las que vamos a inflar
    class ViewHolder(private val binding: GameElementBinding): RecyclerView.ViewHolder(binding.root){
        val ivIcon = binding.ivIcon

        //Vinculamos el game entity , y los ligamos
        fun bind(game: GameEntity){
            //Es lo mismo que la parte de arriba
            /* con el binding.aply , es una funcion de alcance ,para ahorrar codigo */
            binding.apply {
                //-------------------------------------------------------------
                // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
                // -------------------------------------------------------------
                tvTitle.text = game.title
                tvEspecialidad.text = game.speciality
                tvDeveloper.text = game.developer
                tvArea.text = game.area
            }
        }
    }

    //Instanciamos un objeto del view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //Le mandamos el numero de juegos y le retornomos el total de numeros (simplifica el codigo)
    //override fun getItemCount(): Int = games.size
    override fun getItemCount(): Int = games.count()


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)

        holder.itemView.setOnClickListener {
            onGameClick(game)
        }

        holder.ivIcon.setOnClickListener {

        }

        //Mandamos a llamar nuestras especialidades con el spinner
        holder.ivIcon.setImageResource(getIconWithGenreId(game.speciality_id))
    }

    //Nuestro arreglo de spinners
    private fun getIconWithGenreId(genreId: Int): Int{
        return when (genreId){
            0 -> R.drawable.med_gral
            1 -> R.drawable.red
            2 -> R.drawable.nefro
            3 -> R.drawable.ped
            4 -> R.drawable.onco
            5 -> R.drawable.derma
            else -> R.drawable.no_genre_icon
        }
    }


    fun updateList(list: List<GameEntity>){
        games = list
        notifyDataSetChanged()
    }
}