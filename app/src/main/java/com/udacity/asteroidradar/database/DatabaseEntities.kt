package com.udacity.asteroidradar.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.AsteroidDetails
import com.udacity.asteroidradar.domain.AsteroidMain

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

fun List<DatabaseAsteroid>.asDomainModelAsteroidMain() : List<AsteroidMain>{
    return map {
        AsteroidMain(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun DatabaseAsteroid.asDomainModelAsteroidDetails() : AsteroidDetails {
    return AsteroidDetails(
            id = id,
            codename = codename,
            closeApproachDate = closeApproachDate,
            absoluteMagnitude = absoluteMagnitude,
            estimatedDiameter = estimatedDiameter,
            relativeVelocity = relativeVelocity,
            distanceFromEarth = distanceFromEarth,
            isPotentiallyHazardous = isPotentiallyHazardous
        )
}