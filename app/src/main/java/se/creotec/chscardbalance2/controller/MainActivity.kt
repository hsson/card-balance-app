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
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.*
import se.creotec.chscardbalance2.service.AbstractBackendService
import se.creotec.chscardbalance2.service.BalanceService
import se.creotec.chscardbalance2.service.MenuService
import se.creotec.chscardbalance2.util.Util
import java.util.*

class MainActivity : AppCompatActivity(), FoodRestaurantFragment.OnListFragmentInteractionListener,
        OnCardDataChangedListener, OnMenuDataChangedListener, IModel.OnServiceFailedListener {
    private var parentView: View? = null
    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var quickChargeFAB: FloatingActionButton? = null

    private var cardOwnerName: TextView? = null
    private var cardNumber: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentView = findViewById(R.id.main_activity_parent)
        val global = application as GlobalState
        global.model.addCardDataListener(this)
        global.model.addMenuDataListener(this)
        global.model.addServiceFailedListener(this)

        setupAppBar()
        setupFAB()

        setCardData(global.model.cardData)
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
        runOnUiThread {
            swipeRefresh?.isRefreshing = false
            setCardData(newData)
        }
    }

    override fun menuDataChanged(newData: MenuData) {
        Log.i(LOG_TAG, "Menu data was updated")
        runOnUiThread { swipeRefresh?.isRefreshing = false }
    }

    override fun serviceFailed(service: AbstractBackendService<*>, error: String) {
        runOnUiThread {
            swipeRefresh?.isRefreshing = false
            parentView?.let {
                if (service is BalanceService) {
                    Snackbar.make(it, R.string.error_card_failed, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_retry, { _ ->
                                val updateBalance = Intent(this, BalanceService::class.java)
                                updateBalance.action = Constants.ACTION_UPDATE_CARD
                                sendUpdateRequest(updateBalance)
                            }).show()
                } else if (service is MenuService) {
                    Snackbar.make(it, R.string.error_menu_failed, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_retry, { _ ->
                                val updateMenu = Intent(this, MenuService::class.java)
                                updateMenu.action = Constants.ACTION_UPDATE_MENU
                                sendUpdateRequest(updateMenu)
                            }).show()
                }
            }
        }
    }

    // Sets up the appbar
    private fun setupAppBar() {
        val toolbar = findViewById(R.id.toolbar_main) as Toolbar
        setSupportActionBar(toolbar)
        collapsingToolbarLayout = findViewById(R.id.toolbar_collapsing_layout) as CollapsingToolbarLayout
        swipeRefresh = findViewById(R.id.swipe_refresh_container) as SwipeRefreshLayout
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

        swipeRefresh?.let {
            it.setOnRefreshListener {
                maybeUpdate(force = true)
            }
            it.setColorSchemeResources(R.color.color_primary, R.color.color_accent)
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

    private fun maybeUpdate(force: Boolean = false) {
        val model = (application as GlobalState).model
        if (model.cardLastTimeUpdated < HALF_HOUR_AGO || !isDateToday(model.cardLastTimeUpdated) || force) {
            val updateCardIntent = Intent(this, BalanceService::class.java)
            updateCardIntent.action = Constants.ACTION_UPDATE_CARD
            sendUpdateRequest(updateCardIntent)
        }

        if (model.menuLastTimeUpdated < THREE_HOURS_AGO || !isDateToday(model.menuLastTimeUpdated) || force) {
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
            swipeRefresh?.isRefreshing = true
        } else {
            swipeRefresh?.isRefreshing = false
            parentView?.let {
                Snackbar.make(it, R.string.error_no_internet, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_connect, { _ ->
                            startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
                        }).show()
            }
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
