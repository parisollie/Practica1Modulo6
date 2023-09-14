package com.pjff.videogamesdb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.util.Constants.DATABASE_GAME_TABLE

@Dao
interface GameDao {
    //--------------------------------------------------------------------------
    //Aqui definimos las operaciones CRUD : CREATE,READ, UPDATE,Y DELETE

    //--------------------------------------------------------------------------

    //Create
    @Insert
    //Recibe un parametro Game entity
    //Usamos una funcion suspendida, para que se ejecute sin afectar la aplicacion
    // La funcion se ejecuta en una ecorutina
    suspend fun insertGame(game: GameEntity)

    @Insert
    suspend fun insertGame(games: List<GameEntity>)

    //Read
    //Interpolamos(${DATABASE_GAME_TABLE}) y le decimos que de ahi viene
    //Pasame todos los datos de esta tabala ${DATABASE_GAME_TABLE}
    @Query("SELECT * FROM ${DATABASE_GAME_TABLE}")
    //regresame una lista de GameEntity
    suspend fun getAllGames(): List<GameEntity>

    //Update
    @Update
    suspend fun updateGame(game: GameEntity)

    //Delete
    @Delete
    suspend fun deleteGame(game: GameEntity)
}