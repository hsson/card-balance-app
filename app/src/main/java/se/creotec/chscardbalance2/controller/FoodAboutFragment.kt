package se.creotec.chscardbalance2.controller

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import org.w3c.dom.Text
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.OpenHour
import se.creotec.chscardbalance2.model.Restaurant
import se.creotec.chscardbalance2.util.Util
import java.util.*

class FoodAboutFragment : Fragment() {

    private var restaurant: Restaurant = Restaurant("")

    private var websiteButton: Button? = null
    private var rating: RatingBar? = null
    private var openNow: TextView? = null
    private var openHours: TextView? = null
    private var address: TextView? = null
    private var priceEstimate: TextView? = null

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
        openNow = view.findViewById(R.id.restaurant_about_open_now) as TextView
        openHours = view.findViewById(R.id.restaurant_about_open_hours) as TextView
        priceEstimate = view.findViewById(R.id.restaurant_about_avg_price) as TextView

        rating?.rating = restaurant.rating
        setOpenHours(openNow, openHours)
        websiteButton?.setOnClickListener {
            val webIntent = CustomTabsIntent.Builder()
                    .setToolbarColor(activity.getColor(R.color.color_primary))
                    .build()
            webIntent.launchUrl(context, Uri.parse(restaurant.websiteUrl))
        }

        if (restaurant.averagePrice != 0) {
            priceEstimate?.text = getString(R.string.restaurant_about_avg_price, restaurant.averagePrice)
        } else {
            priceEstimate?.text = getString(R.string.restaurant_about_avg_no_price)
        }

        return view
    }

    private fun setOpenHours(openNow: TextView?, openHours: TextView?) {
        if (restaurant.dishes.isEmpty()) {
            showClosed(openNow, openHours, false)
        } else {
            val c = Calendar.getInstance()
            c.time = Date()
            restaurant.openHours.forEach { oh ->
                if (oh.dayOfWeek == c.get(Calendar.DAY_OF_WEEK)) {
                    if (Util.isBetweenHours(oh.startHour, oh.endHour)) {
                        openNow?.text = getString(R.string.restaurant_about_open_now)
                        openNow?.setTextColor(ContextCompat.getColor(activity, R.color.color_success))
                        val startFormatted = DateUtils.formatDateTime(activity, OpenHour.toUnixTimeStamp(oh.startHour), DateUtils.FORMAT_SHOW_TIME)
                        val endFormatted = DateUtils.formatDateTime(activity, OpenHour.toUnixTimeStamp(oh.endHour), DateUtils.FORMAT_SHOW_TIME)
                        openHours?.text = getString(R.string.restaurant_about_hours_range, startFormatted, endFormatted)
                        return
                    } else {
                        showClosed(openNow, openHours, true)
                        return
                    }
                }
            }
            openNow?.text = getString(R.string.restaurant_about_open_today)
            openNow?.setTextColor(ContextCompat.getColor(activity, R.color.color_success))
            openHours?.text = ""
        }
    }

    private fun showClosed(openNow: TextView?, openHours: TextView?, now: Boolean) {
        if (now) {
            openNow?.text = getString(R.string.restaurant_about_closed_now)
        } else {
            openNow?.text = getString(R.string.restaurant_about_closed_today)
        }
        openHours?.text = ""
        openNow?.setTextColor(ContextCompat.getColor(activity, R.color.color_fail))
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
