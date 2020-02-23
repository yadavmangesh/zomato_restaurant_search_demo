package com.mangesh.sugerbox.serivce

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat


class RestaurantsNearBy(private val context: Context): Service(),LocationListener {


    init {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           requestPermission()
        }else{
            getLocation()
        }
    }

     var checkGPS = false

     var checkNetwork = false

     var canGetLocation = false

      var loc: Location?=null

     var latitude: Double? = 0.toDouble()

     var longitude: Double? = 0.toDouble()


    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 100f

    private val MIN_TIME_BW_UPDATES = (1000).toLong()

    private var locationManager: LocationManager? = null


    fun getLocation(): Location? {

        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // get GPS status
            checkGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // get network provider status
            checkNetwork = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(context, "No Service Provider is available", Toast.LENGTH_SHORT).show()
            } else {
                this.canGetLocation = true

                if (checkNetwork) {

                    if (ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermission()

                    } else {
                        loc= requestLocation(LocationManager.NETWORK_PROVIDER)

                    }

                }

                if (checkGPS) {

                    if (ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermission()

                    } else {
                        loc= requestLocation(LocationManager.GPS_PROVIDER)

                    }

                }



            }

        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            return loc
        }


    }

  
    @SuppressLint("MissingPermission")
    private fun requestLocation(provider:String): Location? {

        locationManager!!.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)

        if (locationManager != null) {
            loc = locationManager?.getLastKnownLocation(provider)!!
            if (loc != null) {
                latitude = loc?.latitude
                longitude = loc?.longitude
            }
        }
        return loc
    }

    private fun requestPermission(){

        ActivityCompat.requestPermissions(context as Activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),2)
    }

    fun getLongit(): Double ?{
        if (loc != null) {
            longitude = loc?.longitude
        }
        return longitude
    }

    fun getLat(): Double? {
        if (loc != null) {
            latitude = loc?.latitude
        }
        return latitude
    }

    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

    }

    override fun onProviderEnabled(s: String) {

    }

    override fun onProviderDisabled(s: String) {

    }


}