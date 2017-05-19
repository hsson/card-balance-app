package se.creotec.chscardbalance2.controller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.nostra13.universalimageloader.core.ImageLoader

import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.controller.FoodRestaurantFragment.OnListFragmentInteractionListener
import se.creotec.chscardbalance2.model.Restaurant

class FoodRestaurantRecyclerViewAdapter(private val restaurants: List<Restaurant>, private val listener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<FoodRestaurantRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_foodmenu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant: Restaurant = restaurants[position]
        holder.restaurant = restaurant
        ImageLoader.getInstance().displayImage(restaurant.imageUrl, holder.restaurantHeader)
        holder.restaurantName.text = restaurant.name

        // Show restaurant as closed if it has no dishes
        if (restaurant.dishes.isNotEmpty()) {
            holder.restaurantClosed.visibility = View.GONE
            holder.restaurantHeaderTint.visibility = View.GONE
            holder.restaurantHeaderGradient.visibility = View.VISIBLE
            holder.setDishesCount(restaurant.dishes.size)
        } else {
            holder.restaurantClosed.visibility = View.VISIBLE
            holder.restaurantHeaderTint.visibility = View.VISIBLE
            holder.restaurantHeaderGradient.visibility = View.GONE
        }

        holder.holderView.setOnClickListener {
            listener?.onListFragmentInteraction(restaurant)
        }
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    inner class ViewHolder(val holderView: View) : RecyclerView.ViewHolder(holderView) {
        val restaurantHeader: ImageView = holderView.findViewById(R.id.restaurant_image) as ImageView
        val restaurantHeaderGradient: ImageView = holderView.findViewById(R.id.restaurant_image_gradient) as ImageView
        val restaurantHeaderTint: ImageView = holderView.findViewById(R.id.restaurant_image_tint) as ImageView
        val restaurantName: TextView = holderView.findViewById(R.id.restaurant_name) as TextView
        val restaurantDishCount: TextView = holderView.findViewById(R.id.restaurant_dishes_count) as TextView
        val restaurantClosed: TextView = holderView.findViewById(R.id.restaurant_closed_today) as TextView

        var restaurant: Restaurant? = null

        override fun toString(): String {
            return super.toString() + " '" + restaurant?.toString() + "'"
        }

        internal fun setDishesCount(count: Int) {
            restaurantDishCount.text = holderView.resources.getString(R.string.restaurant_dishes_count, count)
        }
    }
}
