package com.example.recycle.data.fake

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.recycle.data.model.Container
import com.example.recycle.data.model.WasteType
import java.time.DayOfWeek

/**
 * Simula los contenedores del pueblo.
 * Sustituye al backend o hardware real.
 */
object FakeContainerDataSource {

    @RequiresApi(Build.VERSION_CODES.O)
    private val containers = listOf(

        Container(
            id = "container_org_01",
            type = WasteType.ORGANIC,
            latitude = 41.3870,
            longitude = 2.1700,
            isActive = true,
            isFull = false,
            allowedDays = listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY
            )
        ),

        Container(
            id = "container_plastic_01",
            type = WasteType.PLASTIC,
            latitude = 41.3871,
            longitude = 2.1701,
            isActive = true,
            isFull = true,
            allowedDays = listOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY
            )
        ),

        Container(
            id = "container_paper_01",
            type = WasteType.PAPER,
            latitude = 41.3869,
            longitude = 2.1698,
            isActive = false,
            isFull = false,
            allowedDays = listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.THURSDAY
            )
        ),

        Container(
            id = "container_glass_01",
            type = WasteType.GLASS,
            latitude = 41.3872,
            longitude = 2.1703,
            isActive = true,
            isFull = false,
            allowedDays = DayOfWeek.values().toList()
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllContainers(): List<Container> = containers

    @RequiresApi(Build.VERSION_CODES.O)
    fun getContainerById(id: String): Container? =
        containers.find { it.id == id }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getContainersByType(type: WasteType): List<Container> =
        containers.filter { it.type == type }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNearbyContainers(
        latitude: Double,
        longitude: Double,
        maxDistanceMeters: Double = 50.0
    ): List<Container> {
        return containers.filter {
            distanceInMeters(
                latitude,
                longitude,
                it.latitude,
                it.longitude
            ) <= maxDistanceMeters
        }
    }

    private fun distanceInMeters(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val dx = lat1 - lat2
        val dy = lon1 - lon2
        return kotlin.math.sqrt(dx * dx + dy * dy) * 111_000
    }
}
