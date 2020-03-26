package com.mangesh.myglamm.ViewModel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mangesh.myglamm.Model.Restaurant
import com.mangesh.myglamm.Repository.NetWorkRepository

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