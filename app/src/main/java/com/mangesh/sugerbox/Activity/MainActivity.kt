package com.mangesh.sugerbox.Activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.mangesh.sugerbox.R
import com.mangesh.sugerbox.RestaurantApplication
import com.mangesh.sugerbox.ViewModel.MainActivityViewModel
import com.mangesh.sugerbox.adapter.RestaurantAdapter
import com.mangesh.sugerbox.serivce.RestaurantsNearBy
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var viewModel:MainActivityViewModel= MainActivityViewModel(RestaurantApplication.applicationContext() as Application)


    lateinit var restaurantsNearBy:RestaurantsNearBy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        restaurantsNearBy= RestaurantsNearBy(this)


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            restaurantsNearBy.getLocation()?.let { viewModel.getNearByRestaurants(it) }
        }

        viewModel.restaurantList.observe(this, Observer {
            Log.d("MainActivity" ,"Size ${it.size}")

                rv_restaurant.apply {
                    progressBar.visibility= View.GONE
                    layoutManager=LinearLayoutManager(this@MainActivity)
                    adapter=RestaurantAdapter(this@MainActivity,it)
                }


        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            2 -> {
                if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                      displayLocationSettingsRequest(this)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            5 -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.d("MainActivity", "User agreed to make required location settings changes.")
                    restaurantsNearBy.getLocation()?.let { viewModel.getNearByRestaurants(it) }


                }
                Activity.RESULT_CANCELED ->
                    Log.d("MainActivity", "User chose not to make required location settings changes."
                )
            }
        }
    }

    private fun displayLocationSettingsRequest(context: Context) {

        val googleApiClient = GoogleApiClient.Builder(context).addApi(LocationServices.API).build()

        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        locationRequest.fastestInterval = (2000/ 2).toLong()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

        result.setResultCallback(object : ResultCallback<LocationSettingsResult> {

            override fun onResult(result: LocationSettingsResult) {

                val status = result.status

                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {

                        Log.d("MainActivity", "All location settings are satisfied.")


                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.d(
                            "MainActivity",
                            "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                        )

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(
                                context as Activity?, 5
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            Log.d("MainActivity", "PendingIntent unable to execute request.")
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        Log.d(
                            "MainActivity",
                            "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                        )
                }
            }
        })
    }

}
