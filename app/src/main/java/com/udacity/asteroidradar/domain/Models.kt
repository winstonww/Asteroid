package com.udacity.asteroidradar.domain

import android.os.Parcelable
import android.transition.Visibility
import android.view.View
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AsteroidMain constructor(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val isPotentiallyHazardous: Boolean) : Parcelable {
}

@Parcelize
data class AsteroidDetails(val id: Long, val codename: String, val closeApproachDate: String,
                           val absoluteMagnitude: Double, val estimatedDiameter: Double,
                           val relativeVelocity: Double, val distanceFromEarth: Double,
                           val isPotentiallyHazardous: Boolean) : Parcelable



