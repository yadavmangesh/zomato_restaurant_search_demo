package com.mangesh.sugerbox.ViewModel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.mangesh.sugerbox.Model.Restaurant
import com.mangesh.sugerbox.Repository.NetWorkRepository
import com.mangesh.sugerbox.serivce.RestaurantsNearBy

class MainActivityViewModel constructor(application: Application):AndroidViewModel(application){

    val repository=NetWorkRepository(application)

    var restaurantList:MutableLiveData<MutableList<Restaurant>>

    init {
       restaurantList=repository.restaurantsList
    }



    fun getNearByRestaurants(location: Location){

        repository.getRestaurants(location.latitude.toString(),location.longitude.toString())
    }


}