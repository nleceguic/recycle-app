package com.example.recycle.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.recycle.databinding.ActivityMainBinding
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.registerForActivityResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        when {
            fineGranted -> {
                onLocationPermissionGranted(highAccuracy = true)
            }

            else -> {
                onLocationPermissionDenied()
            }
        }
    }
    private val locationSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            onLocationEnabled()
        } else {
            onLocationRejected()
        }
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermissions()

        binding.openContainerButton.setOnClickListener {
            checkLocationSettings()
        }
    }

    private fun checkLocationPermissions() {
        if (hasLocationPermissions()) {
            onLocationPermissionGranted(
                highAccuracy = hasFineLocationPermission()
            )
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun hasLocationPermissions(): Boolean {
        return hasFineLocationPermission()
    }

    private fun hasFineLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun onLocationPermissionGranted(highAccuracy: Boolean) {
        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude

                Log.d("LOCATION", "Lat: $lat, Lon: $lon")
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicacion", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onLocationPermissionDenied() {
        Toast.makeText(
            this, "Permisos de ubicación denegados", Toast.LENGTH_LONG
        ).show()
    }

    fun checkLocationSettings() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000
        ).build()

        val settingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(true).build()

        val client = LocationServices.getSettingsClient(this)

        client.checkLocationSettings(settingsRequest).addOnSuccessListener {
            onLocationEnabled()
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettingsLauncher.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            } else {
                onLocationRejected()
            }
        }
    }

    private fun onLocationEnabled() {
        Toast.makeText(
            this, "Ubicación activada", Toast.LENGTH_SHORT
        ).show()
    }

    private fun onLocationRejected() {
        Toast.makeText(
            this, "La app necesita la ubicación para funcionar", Toast.LENGTH_LONG
        ).show()
    }
}