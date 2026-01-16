package com.example.recycle.data.fake

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.recycle.data.model.Incident
import com.example.recycle.data.model.IncidentStatus
import com.example.recycle.data.model.IncidentType
import com.example.recycle.data.model.WasteType
import java.time.LocalDateTime
import java.util.UUID

object FakeIncidentRepository {
    private val incidents = mutableListOf<Incident>()

    fun getAllIncidents(): List<Incident> = incidents.sortedByDescending { it.timestamp }

    fun getOpenIncidents(): List<Incident> =
        incidents.filter { it.status != IncidentStatus.RESOLVED }

    @RequiresApi(Build.VERSION_CODES.O)
    fun reportIncident(
        containerId: String?, wasteType: WasteType?, type: IncidentType, description: String
    ): Incident {
        val incident = Incident(
            id = UUID.randomUUID().toString(),
            containerId = containerId,
            wasteType = wasteType,
            type = type,
            description = description,
            timestamp = LocalDateTime.now(),
            status = IncidentStatus.REPORTED
        )

        incidents.add(incident)
        return incident
    }

    fun updateIncidentStatus(incidentId: String, newStatus: IncidentStatus) {
        val index = incidents.indexOfFirst { it.id == incidentId }
        if (index != -1) {
            incidents[index] = incidents[index].copy(status = newStatus)
        }
    }

    fun clear() {
        incidents.clear()
    }
}