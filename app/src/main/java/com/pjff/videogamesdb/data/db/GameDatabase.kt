package com.pjff.videogamesdb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pjff.videogamesdb.data.db.model.GameEntity
import com.pjff.videogamesdb.util.Constants

//-----------------------------ES NUESTRA BASE DE DATOS -------------------------------------

@Database(
    //Le pasamos las entidades con las que trabajaremos nuestra base de datos
    entities = [GameEntity::class],
    //SI AGREGO UN CAMPO MAS ESTO SE DEBE ACTUALIZAR TAMBIEN *****
    version = 9,  //versión de la BD. Importante para las migraciones
    exportSchema = true //por defecto esta en true
)
//Para que funcione con room debe ser abstract class
abstract class GameDatabase: RoomDatabase() { //Tiene que se abstracta

    //Aquí va el DAO
    abstract fun gameDao(): GameDao

    //Sin inyección de dependencias, metemos la creación de la bd con un singleton aquí
    companion object{

        //Esto es codigo raro de google, que no le entendamos aún

        @Volatile //lo que se escriba en este campo, será inmediatamente visible a otros hilos
        //Esto para que al momento de instanciar la base de datos haga la instancia
        private var INSTANCE: GameDatabase? = null

        //Ponemos el contexto
        fun getDatabase(context: Context): GameDatabase {
            //Nos regresa  una instancia , syn ,tiene que ver con los hilos
            return INSTANCE ?: synchronized(this){
                //Si la instancia no es nula, entonces se regresa
                // si es nula, entonces se crea la base de datos (patrón singleton)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    //Le mandamos el nombre de nuestra base de datos
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration() //Permite a Room recrear las tablas de la BD si las migraciones no se encuentran
                    .build()

                INSTANCE = instance

                //regresamos instance
                instance
            }
        }
    }

}