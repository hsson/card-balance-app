// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.google.gson.Gson
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.Restaurant
import se.creotec.chscardbalance2.service.BalanceService
import se.creotec.chscardbalance2.service.MenuService
import se.creotec.chscardbalance2.util.Util

class MainActivity : AppCompatActivity(), FoodRestaurantFragment.OnListFragmentInteractionListener {

    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var quickChargeFAB: FloatingActionButton? = null

    private var cardOwnerName: TextView? = null
    private var cardNumber: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupAppBar()
        setupFAB()
        // TODO: Remove debug
        val updateBalanceIntent = Intent(this, BalanceService::class.java)
        updateBalanceIntent.action = Constants.ACTION_UPDATE_CARD
        println("Starting balance service")
        this.startService(updateBalanceIntent)

        // TODO: Remove debug
        val updateMenuIntent = Intent(this, MenuService::class.java)
        updateMenuIntent.action = Constants.ACTION_UPDATE_MENU
        println("Starting menu service")
        this.startService(updateMenuIntent)

        // TODO: Remove debug
        val global = application as GlobalState
        val card = global.model.cardData
        card.ownerName = "John Doe"
        global.model.cardData = card
        global.saveCardData()
        cardOwnerName?.let { it.text = global.model.cardData.ownerName }
        cardNumber?.let { it.text = Util.formatCardNumber(global.model.cardData.cardNumber) }
        collapsingToolbarLayout?.let {
            it.title = global.model.cardData.cardBalance.toString() + " " + Constants.CARD_CURRENCY_SUFFIX
        }
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
                val alpha = 1 - percentage * 2
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
                        .setSecondaryToolbarColor(getColor(R.color.color_accent)) // TODO: Double check
                        .build()
                webIntent.launchUrl(context, Uri.parse(global.model.quickChargeURL))
            }
        }
    }

    // Restaurant was clicked
    override fun onListFragmentInteraction(item: Restaurant) {
        val restaurantIntent = Intent(this, RestaurantPageActivity::class.java)
        val restaurantJSON = Gson().toJson(item, Restaurant::class.java)
        restaurantIntent.putExtra(Constants.INTENT_RESTAURANT_DATA_KEY, restaurantJSON)
        startActivity(restaurantIntent)
    }
}
