package se.creotec.chscardbalance2.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson

import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.Restaurant

class FoodAboutFragment : Fragment() {

    private var restaurant: Restaurant = Restaurant("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            restaurant = Gson().fromJson(it.getString(ARG_RESTAURANT), Restaurant::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_restaurant_about, container, false)
    }

    companion object {
        private val ARG_RESTAURANT = "about_restaurant"
        fun newInstance(restaurant: Restaurant): FoodAboutFragment {
            val fragment = FoodAboutFragment()
            val args = Bundle()
            val restaurantJSON = Gson().toJson(restaurant, Restaurant::class.java)
            args.putString(ARG_RESTAURANT, restaurantJSON)
            fragment.arguments = args
            return fragment
        }
    }
}
