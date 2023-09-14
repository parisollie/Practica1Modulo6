package com.pjff.videogamesdb.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pjff.videogamesdb.R
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import com.pjff.videogamesdb.application.VideogamesDBApp


class MainActivity : AppCompatActivity() {
    //Agreamos el binding
    private lateinit var binding: ActivityMainBinding

    //El listado de juegos que va a tener y la ponemos vacía
    private var games: List<GameEntity> = emptyList()

    //Para que nos instancie nuestro repositorio
    private lateinit var repository: GameRepository

    //Instanciamos el adapter
    private lateinit var gameAdapter: GameAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        //Para tener view binding de cajon

        setContentView(binding.root)

        //Para instanciar el repositorio
        repository = (application as VideogamesDBApp).repository

        //Instanciamos el GameAdapter ,pero le pasamos la funcion Lambda del Game Dialog
        gameAdapter = GameAdapter(){game ->
            gameClicked(game)
        }
        binding.rvGames.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gameAdapter
        }
        updateUI()
    }

    private fun updateUI(){
        lifecycleScope.launch {
            games = repository.getAllGames()
            //Si encontro un juego,por lo menos hay un registro
            if(games.isNotEmpty()){
                //Hay por lo menos un registro,entonces la cajita de texto no se muestra
                //No hay registros !
                binding.tvSinRegistros.visibility = View.INVISIBLE
            }else{
                binding.tvSinRegistros.visibility = View.VISIBLE
            }
            //Aqui ponemos el gameAdapter y ya podemos ver el recycler view
            gameAdapter.updateList(games)
        }
    }

    fun click(view: View) {
        //Aqui le estamos pasando las lambdas
        val dialog = GameDialog( updateUI = {
            //Le pasamos la lambda UpdateUi  de GameDialogy la lambda del mensaje
            updateUI()
        }, message = {id ->
            message(id)
        })
        //La etiqueta dialog es para mostrarlo en pantalla
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun gameClicked(game: GameEntity){
        //Toast.makeText(this, "Click en el juego con id: ${game.id}", Toast.LENGTH_SHORT).show()
        //Se lo mandamos en falso ,para que se generen los botones con actualizar
        //El primer game se refiere al nombre del parametro y el siguiente game es el que me llega
        //Toast.makeText(this, "Click en el juego ${game.title}", Toast.LENGTH_LONG).show()
        val dialog = GameDialog(isNewGame = false, game = game, updateUI = {
            updateUI()
        }, message = {id ->
            message(id)
        })

        dialog.show(supportFragmentManager, "dialog")
    }

    private fun message(id: Int){
        Snackbar.make(binding.cl, getString(id), Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
}