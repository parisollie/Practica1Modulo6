package com.pjff.videogamesdb.application
import android.app.Application
import com.pjff.videogamesdb.data.GameRepository
import com.pjff.videogamesdb.data.db.GameDatabase



class VideogamesDBApp(): Application() {

    private val database by lazy {
        GameDatabase.getDatabase((this@VideogamesDBApp))
    }

    val repository by lazy {
        GameRepository(database.gameDao())
    }

}

