package com.pjff.videogamesdb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pjff.videogamesdb.util.Constants
//Le pasamos las tablas y los datos a capturar
@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class GameEntity(
    //Con Room le ponemos el primaryKey
    @PrimaryKey(autoGenerate = true)
    //Le decimos que se guarde como game_id
    @ColumnInfo(name = "game_id")
    //------------------- LOS CAMPOS DE NUESTRA ENTIDAD -------------------------------

    //Este id quiero que se use como llave primaria,empieza en 0,porque se va autoincrementar
    val id: Long = 0,

    //-------------------------------------------------------------
    // AQUI AGREGAREMOS CODIGO PARA LA PRACTICA 1
    // -------------------------------------------------------------

    @ColumnInfo(name = "game_title")
    var title: String,

    @ColumnInfo(name = "doctor_speciality")
    var speciality: String,

    @ColumnInfo(name = "doctor_speciality_id")
    var speciality_id: Int,

    @ColumnInfo(name = "game_developer")
    var developer: String,

    @ColumnInfo(name = "doctor_area")
    var area: String
)
