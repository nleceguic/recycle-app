package com.example.recycle.ui.home

import androidx.lifecycle.ViewModel
import com.example.recycle.data.fake.FakeContainerDataSource
import com.example.recycle.data.fake.FakeDisposalRepository
import com.example.recycle.data.model.Container
import com.example.recycle.data.model.DisposalEvent
import com.example.recycle.data.model.FailureReason
import com.example.recycle.data.model.WasteType

class HomeViewModel : ViewModel() {
    private var nearbyContainers: List<Container> = emptyList()

    // Detección de contenedores cercanos.
    fun detectNearbyContainers(userLatitude: Double, userLongitude: Double): List<Container> {
        nearbyContainers = FakeContainerDataSource.getNearbyContainers(
            latitude = userLatitude, longitude = userLongitude
        )
        return nearbyContainers
    }

    // Selecciona el contenedor adecuado.
    fun selectContainer(wasteType: WasteType): Container? {
        return nearbyContainers.firstOrNull { it.type == wasteType }
    }

    // Intenta abrir el contenedor seleccionado.
    fun openContainer(container: Container): OpenResult {
        val event: DisposalEvent = FakeDisposalRepository.tryDispose(
            containerId = container.id,
            wasteType = container.type,
            allowedDays = container.allowedDays,
            isContainerActive = container.isActive,
            isContainerFull = container.isFull
        )

        return if (event.wasSuccessful) {
            OpenResult.Success
        } else {
            OpenResult.Failure(event.failureReason ?: FailureReason.UNKNOWN)
        }
    }
}