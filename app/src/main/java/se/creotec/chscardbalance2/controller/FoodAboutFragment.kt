package se.creotec.chscardbalance2.controller

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.google.gson.Gson
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.Restaurant

class FoodAboutFragment : Fragment() {

    private var restaurant: Restaurant = Restaurant("")

    private var websiteButton: Button? = null
    private var rating: RatingBar? = null
    private var openNow: EditText? = null
    private var openHours: EditText? = null
    private var address: EditText? = null
    private var priceEstimate: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            restaurant = Gson().fromJson(it.getString(ARG_RESTAURANT), Restaurant::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restaurant_about, container, false)
        rating = view.findViewById(R.id.restaurant_rating_bar) as RatingBar
        websiteButton = view.findViewById(R.id.restaurant_visit_website) as Button

        rating?.rating = restaurant.rating
        websiteButton?.setOnClickListener {
            val webIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(activity.getColor(R.color.color_primary))
                    .build()
            webIntent.launchUrl(context, Uri.parse(restaurant.websiteUrl))
        }
        return view
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
