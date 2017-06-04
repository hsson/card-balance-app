// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.controller.FoodDishFragment.OnListFragmentInteractionListener
import se.creotec.chscardbalance2.model.Dish
import se.creotec.chscardbalance2.util.Util

class FoodDishRecyclerViewAdapter(private val dishes: List<Dish>, private val listener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<FoodDishRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_dish, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish: Dish = dishes[position]
        holder.dish = dish
        holder.dishTitle.text = Util.capitalizeAllWords(dish.title)
        holder.dishDesc.text = Util.capitalizeAllWords(dish.description)
        holder.dishContainer.setOnLongClickListener(View.OnLongClickListener {
            listener?.let {
                it.onListFragmentInteraction(dish)
                return@OnLongClickListener true
            }
            false
        })
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    inner class ViewHolder(val dishContainer: View) : RecyclerView.ViewHolder(dishContainer) {
        val dishTitle: TextView = dishContainer.findViewById(R.id.dish_title) as TextView
        val dishDesc: TextView = dishContainer.findViewById(R.id.dish_desc) as TextView
        var dish: Dish? = null

        override fun toString(): String {
            return super.toString() + " '" + dish?.toString() + "'"
        }
    }
}
