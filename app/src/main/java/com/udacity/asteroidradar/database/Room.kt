package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDatabseDao {
    @Query("SELECT * FROM databaseasteroid")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(vararg asteroids: DatabaseAsteroid)

    @Query("SELECT * FROM databaseasteroid WHERE id = :id")
    fun getAsteroidById(id: Long): List<DatabaseAsteroid>
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val dao: AsteroidDatabseDao
}

private lateinit var INSTANCE : AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, AsteroidDatabase::class.java, "asteroids"
            ).build()
        }
    }
    return INSTANCE
}

