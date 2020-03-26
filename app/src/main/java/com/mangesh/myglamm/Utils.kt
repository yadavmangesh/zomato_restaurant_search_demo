package com.mangesh.myglamm

import com.mangesh.myglamm.Model.Restaurant
import org.json.JSONArray
import org.json.JSONObject

class Utils {

    companion object{

        fun parseJson(response:String):MutableList<Restaurant>{

            val restaurantList:MutableList<Restaurant> = mutableListOf()
            val responseObject=JSONObject(response)
            val restaurantArray:JSONArray=responseObject.optJSONArray("restaurants")

            for (i in 0 until restaurantArray.length()){

                val mainObject= restaurantArray[i] as JSONObject

                val restaurantObject= mainObject.optJSONObject("restaurant")

                val restaurantName=restaurantObject?.optString("name")
                val restaurantImage=restaurantObject?.optString("featured_image")
                val restaurantCusines=restaurantObject?.optString("cuisines")
                val restaurantCost=restaurantObject?.optString("average_cost_for_two")
                val restaurantCurrency=restaurantObject?.optString("currency")

               val restaurant=Restaurant(restaurantName,restaurantImage,restaurantCost,restaurantCurrency,restaurantCusines)
                restaurantList.add(restaurant)
            }

            return restaurantList
        }
    }
}