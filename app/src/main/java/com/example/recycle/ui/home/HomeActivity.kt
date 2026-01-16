package com.example.recycle.ui.home


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.recycle.databinding.ActivityMainBinding
import androidx.core.view.isVisible
import com.example.recycle.ui.history.HistoryActivity
import com.example.recycle.ui.map.MapActivity
import com.example.recycle.ui.report.ReportActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.recycle.data.model.FailureReason
import com.example.recycle.data.model.WasteType
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (granted) {
                getCurrentLocation()
            }
        }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var uLat: Double = 0.0
    private var uLon: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.detectNearbyContainers(
            userLatitude = 41.3870, userLongitude = 2.1700
        )

        binding.openContainerButton.setOnClickListener {
            val container = viewModel.selectContainer(WasteType.ORGANIC)

            if (container == null) {
                showError("No hay contenedores cerca")
                return@setOnClickListener
            }

            when (val result = viewModel.openContainer(container)) {
                is OpenResult.Success -> {
                    showSuccess()
                }

                is OpenResult.Failure -> {
                    showFailure(result.reason)
                }
            }
        }

        val showBanner = false
        binding.statusBanner.isVisible = showBanner

        binding.llHistorial.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.reportIssueButton.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        binding.llMapa.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(
            this, message, Toast.LENGTH_LONG
        ).show()
    }

    private fun showSuccess() {
        Toast.makeText(
            this, "Contenedor abierto correctamente", Toast.LENGTH_SHORT
        ).show()
    }

    private fun showFailure(reason: FailureReason) {
        val message = when (reason) {
            FailureReason.WRONG_DAY -> "Hoy no se puede tirar este residuo"

            FailureReason.CONTAINER_FULL -> "El contenedor está lleno"

            FailureReason.CONTAINER_DISABLED -> "Contenedor fuera de servicio"

            FailureReason.NOT_IN_RANGE -> "No estás lo suficientemente cerca"

            FailureReason.BLUETOOTH_ERROR -> "Error de conexión"

            FailureReason.UNKNOWN -> "Error desconocido"
        }

        showError(message)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    uLat = location.latitude
                    uLon = location.longitude
                    val accuracy = location.accuracy

                    viewModel.detectNearbyContainers(userLatitude = uLat, userLongitude = uLon)
                } else {
                    showError("No se pudo obtener la ubicación")
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun comprobarPermisoYLlamar() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            getCurrentLocation()
        }
    }
}