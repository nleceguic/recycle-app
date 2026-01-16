package com.example.recycle.data.model

import java.time.LocalDateTime

data class Incident(
    val id: String,

    val containerId: String?,

    val wasteType: WasteType?,

    val type: IncidentType,

    val description: String,

    val timestamp: LocalDateTime,

    val status: IncidentStatus
)