package com.pjff.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.pjff.videogamesdb.R

import com.pjff.videogamesdb.application.VideogamesDBApp
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.databinding.GameDialogBinding
import kotlinx.coroutines.launch
import java.io.IOException


class GameDialog(
    //Cuando instancie un objeto le pasare un valor por defecto,para que al dar click
    //no los enseñe y se pinte
    private  var isNewGame: Boolean = true,
    //Si no pasa nada tu generate tu propio game entity
    private  var game:GameEntity = GameEntity(
        //-------------------------------------------------------------
        // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
        // -------------------------------------------------------------
        title = "",
        speciality = "",
        speciality_id = 0,
        developer = "",
        area = ""
        //Aqui recibi las lamdas , no recibe nada,la primera no regrese nada
    ), private  val updateUI: () -> Unit,
    //Le mandamos otra lambda para que se imprima el mensaje de si quieres eliminar el juego
    private  val message: (Int) -> Unit
): DialogFragment() {
    //Para evitar fugas de memoria
    private var _binding: GameDialogBinding? = null
    //para cuando instanciado el binding
    private val binding get() = _binding!!

    //para generar el Alert dialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog
    //Cuando no se llenen los campos ,invalidamos el boton
    private  var saveButton: Button? = null

    //El repositorio es mi interaccion a mi base de datos
    private lateinit var repository: GameRepository

    //Se configura el diálogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Aqui inflamos el binding va ligado a un fragment o a un activity
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        //Instanciamos para el repositorio
        repository = (requireContext().applicationContext as VideogamesDBApp).repository
        //Instanciamos,para ponerle mensajitos al usuario, la ventanita que sale
        builder = AlertDialog.Builder(requireContext())

        setupSpinnerOptions()

        binding.apply {
            tietTitle.setText(game.title)
            tietDeveloper.setText(game.developer)

            SpecialitySpinner.setSelection(game.speciality_id)
            tietArea.setText(game.area)
        }
        //Variable para nuestro spinner
        val array: Array<String> = resources.getStringArray(R.array.espe)

        //Si la banderia esta en true
        dialog = if(isNewGame){
            buildDialog(getString(R.string.save_title), getString(R.string.cancel_title), {
                //-------------------------------------------------------------
                // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
                // -------------------------------------------------------------
                // Guardar
                game.title = binding.tietTitle.text.toString()
                game.speciality = array[binding.SpecialitySpinner.selectedItemPosition]
                game.developer = binding.tietDeveloper.text.toString()
                game.speciality_id = binding.SpecialitySpinner.selectedItemPosition
                game.area = binding.tietArea.text.toString()

                try{
                    //Aqui ya tenemos nuestra ecorutina
                    lifecycleScope.launch {
                        repository.insertGame(game)

                    }
                    //El juego guardado es sin hacer hard coding
                    //deberia hacerse así : R.string.juego_guardado y se guarda en strings
                    //message(getString(R.string.juego_guardado))

                    message(R.string.save_message)

                    updateUI()
                }catch(e: IOException){
                    message(R.string.save_error_message)
                }
            }, {
                // Cancelar
            })
        }else{
            //Y sino lo que mostrara en los botones sera esto
            buildDialog(getString(R.string.update_title), getString(R.string.delete_title), {
                // Update
                //-------------------------------------------------------------
                // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
                // -------------------------------------------------------------
                //**************** Esta lambda es para el Update **********************
                game.title = binding.tietTitle.text.toString()
                game.speciality = array[binding.SpecialitySpinner.selectedItemPosition]
                game.developer = binding.tietDeveloper.text.toString()
                game.speciality_id = binding.SpecialitySpinner.selectedItemPosition
                game.area = binding.tietArea.text.toString()

                try{
                    lifecycleScope.launch {
                        repository.updateGame(game)
                    }

                    message(R.string.update_message)

                    updateUI()
                }catch(e: IOException){
                    message(R.string.update_error_message)
                }

            }, {
                //**************** Esta lambda es para el delete  **********************
                // Delete
                //Le mandamos un dialogo al usuario para preguntarle ,si realmente quiere eliminar
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirm_title))
                    .setMessage(getString(R.string.confirm_message, game.title))
                    .setPositiveButton(getString(R.string.acept_title)){ _, _ ->
                        try{
                            lifecycleScope.launch {
                                repository.deleteGame(game)
                            }

                            //Mensajes desde lambdas
                            message(R.string.delete_message)

                            //Actualizar la UI
                            updateUI()
                        }catch(e: IOException){
                            message(R.string.delete_error_message)
                        }
                        //si el usuario dice que no
                    }.setNegativeButton(getString(R.string.cancel_title)){dialog, _ ->
                        dialog.dismiss()
                    }.create()
                    .show()
            })
        }

        return dialog
    }

    //Logica para mi spinner
    private fun setupSpinnerOptions(){
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.espe,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.SpecialitySpinner.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    //Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = validateFields()

        binding.tietTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })

        binding.tietDeveloper.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })
    }

    private fun validateFields(): Boolean{
        return (binding.tietTitle.text.toString().isNotEmpty() && binding.tietDeveloper.text.toString().isNotEmpty())
    }
    //Funcion para crear lo que quiero que tengan los botones del dialogo que se generen
    private fun buildDialog(btn1Text: String, btn2Text:String,
                            positiveButton: () -> Unit, negativeButton: () -> Unit): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.doc_title))
            .setPositiveButton(btn1Text){dialog, _ ->
                positiveButton()
            }
            .setNegativeButton(btn2Text){dialog, _ ->
                negativeButton()
            }.create()
}