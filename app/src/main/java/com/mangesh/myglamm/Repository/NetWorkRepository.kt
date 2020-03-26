package com.mangesh.myglamm.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.mangesh.myglamm.ApiReponseListener
import com.mangesh.myglamm.Model.Restaurant
import com.mangesh.myglamm.Utils
import com.mangesh.myglamm.VolleyHelper

class NetWorkRepository(context: Context) {

    val volleyHelper=VolleyHelper.getInstance(context)

    val queue:RequestQueue=volleyHelper.requestQueue

    var restaurantsList:MutableLiveData<MutableList<Restaurant>> = MutableLiveData()


    fun getRestaurants( lat:String,lon:String){

        val url="https://developers.zomato.com/api/v2.1/search?lat=$lat&lon=$lon"

        val request=volleyHelper.getRequest(url,object :ApiReponseListener{

            override fun onSuccess(response: String) {
                  restaurantsList.value=Utils.parseJson(response)
            }

            override fun onError(volleyError: VolleyError) {
              Log.d("NetWorkRepository ","onError  ${volleyError.networkResponse}")
            }

        })
        queue.add(request)
    }

}