// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.CardData
import se.creotec.chscardbalance2.model.OnCardDataChangedListener
import se.creotec.chscardbalance2.model.Restaurant
import se.creotec.chscardbalance2.service.BalanceService
import se.creotec.chscardbalance2.service.MenuService
import se.creotec.chscardbalance2.util.Util
import java.util.*

class MainActivity : AppCompatActivity(), FoodRestaurantFragment.OnListFragmentInteractionListener, OnCardDataChangedListener {

    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var quickChargeFAB: FloatingActionButton? = null

    private var cardOwnerName: TextView? = null
    private var cardNumber: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val global = application as GlobalState
        global.model.addCardDataListener(this)

        setupAppBar()
        setupFAB()

        setCardData(global.model.cardData)

        maybeUpdate()
    }

    override fun onResume() {
        super.onResume()
        maybeUpdate()
    }

    // Restaurant was clicked
    override fun onListFragmentInteraction(item: Restaurant) {
        Log.i(LOG_TAG, "${item.name} was clicked")
        val restaurantIntent = Intent(this, RestaurantPageActivity::class.java)
        val restaurantJSON = Gson().toJson(item, Restaurant::class.java)
        restaurantIntent.putExtra(Constants.INTENT_RESTAURANT_DATA_KEY, restaurantJSON)
        startActivity(restaurantIntent)
    }

    override fun cardDataChanged(newData: CardData) {
        Log.i(LOG_TAG, "Card data was updated")
        runOnUiThread { setCardData(newData) }
    }

    // Sets up the appbar
    private fun setupAppBar() {
        val toolbar = findViewById(R.id.toolbar_main) as Toolbar
        setSupportActionBar(toolbar)
        collapsingToolbarLayout = findViewById(R.id.toolbar_collapsing_layout) as CollapsingToolbarLayout
        cardOwnerName = findViewById(R.id.toolbar_card_name) as TextView
        cardNumber = findViewById(R.id.toolbar_card_number) as TextView
        appBarLayout = findViewById(R.id.app_bar_layout) as AppBarLayout

        // Fade out name and card number when app bar is being collapsed
        appBarLayout?.let {
            it.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percentage = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
                val alpha = 1 - percentage * 1.75f
                cardOwnerName?.let { it.alpha = alpha }
                cardNumber?.let { it.alpha = alpha }
            }
        }
    }

    // Adds action to the FAB
    private fun setupFAB() {
        val global = application as GlobalState
        quickChargeFAB = findViewById(R.id.fab_charge_card) as FloatingActionButton
        val context = this
        quickChargeFAB?.let {
            it.setOnClickListener {
                val webIntent = CustomTabsIntent.Builder()
                        .setToolbarColor(getColor(R.color.color_primary))
                        .build()
                webIntent.launchUrl(context, Uri.parse(global.model.quickChargeURL))
            }
        }
    }

    private fun maybeUpdate() {
        val model = (application as GlobalState).model
        if (model.cardLastTimeUpdated < HALF_HOUR_AGO || !isDateToday(model.cardLastTimeUpdated)) {
            val updateCardIntent = Intent(this, BalanceService::class.java)
            updateCardIntent.action = Constants.ACTION_UPDATE_CARD
            sendUpdateRequest(updateCardIntent)
        }

        if (model.menuLastTimeUpdated < THREE_HOURS_AGO || !isDateToday(model.menuLastTimeUpdated)) {
            val updateMenuIntent = Intent(this, MenuService::class.java)
            updateMenuIntent.action = Constants.ACTION_UPDATE_MENU
            sendUpdateRequest(updateMenuIntent)
        }
    }

    private fun sendUpdateRequest(intent: Intent) {
        Log.i(LOG_TAG, "Sending request to update ${intent.action}")
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connManager.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected) {
            startService(intent)
        }
    }

    private fun setCardData(cardData: CardData) {
        cardOwnerName?.let { it.text = cardData.ownerName }
        cardNumber?.let { it.text = Util.formatCardNumber(cardData.cardNumber) }
        collapsingToolbarLayout?.let {
            it.title = "${cardData.cardBalance} ${Constants.CARD_CURRENCY_SUFFIX}"
        }
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.name

        private val HALF_HOUR_AGO: Long = 1000 * 60 * 30
            get() = System.currentTimeMillis() - field
        private val THREE_HOURS_AGO:Long = 1000 * 60 * 60 * 3
            get() = System.currentTimeMillis() - field

        private fun isDateToday(timeInMillis: Long): Boolean {
            val cal = Calendar.getInstance()
            // Set calendar to start of today
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            return timeInMillis >= cal.timeInMillis
        }
    }
}
