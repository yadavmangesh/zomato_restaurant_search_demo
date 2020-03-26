package com.mangesh.myglamm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mangesh.myglamm.Model.Restaurant
import com.mangesh.myglamm.R
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantAdapter(val context: Context, private val restaurantList:MutableList<Restaurant>):
    RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder>() {

    class RestaurantHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RestaurantHolder {
         val layoutInflater=LayoutInflater.from(parent.context)

        return RestaurantHolder(layoutInflater.inflate(R.layout.restaurant_item,parent,false))
    }

    override fun getItemCount(): Int {
        return restaurantList.size
     }

    override fun onBindViewHolder(holder: RestaurantHolder, position: Int) {
        val restaurant=restaurantList[position]

        holder.itemView.tv_name.text=restaurant?.name
        holder.itemView.tv_cusines.text=restaurant?.cuisines

        holder.itemView.tv_cost.text="Cost for two ${restaurant?.currency}${restaurant?.costForTwo} approx"

        Glide.with(context)
            .load(restaurant.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.itemView.iv_image)
    }
}