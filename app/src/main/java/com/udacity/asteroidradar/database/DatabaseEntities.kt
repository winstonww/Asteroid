package com.udacity.asteroidradar.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    @ColumnInfo
    val codename: String,
    @ColumnInfo
    val closeApproachDate: String,
    @ColumnInfo
    val absoluteMagnitude: Double,
    @ColumnInfo
    val estimatedDiameter: Double,
    @ColumnInfo
    val relativeVelocity: Double,
    @ColumnInfo
    val distanceFromEarth: Double,
    @ColumnInfo
    val isPotentiallyHazardous: Boolean)

fun List<DatabaseAsteroid>.asDomainModel() : List<Asteroid>{
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}