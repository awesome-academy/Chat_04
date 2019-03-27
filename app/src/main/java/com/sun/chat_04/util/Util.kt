package com.sun.chat_04.util

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

object Util {

    var local: Location? = null
    @SuppressLint("MissingPermission")
    fun getLocation(locationManager: LocationManager) {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                if (location != null) {
                    local = location
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 2000f, locationListener)
    }
}
