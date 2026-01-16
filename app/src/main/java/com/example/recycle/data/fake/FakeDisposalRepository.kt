package com.example.recycle.data.fake

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.recycle.data.model.DisposalEvent
import com.example.recycle.data.model.FailureReason
import com.example.recycle.data.model.WasteType
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.UUID

object FakeDisposalRepository {
    private val disposalEvents = mutableListOf<DisposalEvent>()

    fun getAllEvents(): List<DisposalEvent> = disposalEvents.sortedByDescending { it.timeStamp }

    fun getEventsByWasteType(type: WasteType): List<DisposalEvent> =
        disposalEvents.filter { it.wasteType == type }

    fun getSuccessfulCount(): Int = disposalEvents.count { it.wasSuccessful }

    fun getFailedCount(): Int = disposalEvents.count { !it.wasSuccessful }

    @RequiresApi(Build.VERSION_CODES.O)
    fun tryDispose(
        containerId: String,
        wasteType: WasteType,
        allowedDays: List<DayOfWeek>,
        isContainerActive: Boolean,
        isContainerFull: Boolean
    ): DisposalEvent {
        val now = LocalDateTime.now()
        val today = now.dayOfWeek

        val failureReason = when {
            !isContainerActive -> FailureReason.CONTAINER_DISABLED
            isContainerFull -> FailureReason.CONTAINER_FULL
            today !in allowedDays -> FailureReason.WRONG_DAY
            else -> null
        }

        val success = failureReason == null

        val event = DisposalEvent(
            id = UUID.randomUUID().toString(),
            containerId = containerId,
            wasteType = wasteType,
            timeStamp = now,
            wasSuccessful = success,
            failureReason = failureReason
        )

        disposalEvents.add(event)
        return event
    }

    fun clear() {
        disposalEvents.clear()
    }
}