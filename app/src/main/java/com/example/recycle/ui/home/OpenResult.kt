package com.example.recycle.ui.home

import com.example.recycle.data.model.FailureReason

sealed class OpenResult {
    object Success : OpenResult()
    data class Failure(val reason: FailureReason) : OpenResult()
}