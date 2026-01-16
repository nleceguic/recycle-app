package com.example.recycle.data.model

import java.time.LocalDateTime

data class DisposalEvent(
    val id: String,

    val containerId: String, val wasteType: WasteType,

    val timeStamp: LocalDateTime,

    val wasSuccessful: Boolean, val failureReason: FailureReason? = null
)