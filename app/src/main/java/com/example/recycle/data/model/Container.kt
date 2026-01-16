package com.example.recycle.data.model

import java.time.DayOfWeek

data class Container(
    val id: String, val type: WasteType,

    val latitude: Double, val longitude: Double,

    val isActive: Boolean, val isFull: Boolean,

    val allowedDays: List<DayOfWeek>
)