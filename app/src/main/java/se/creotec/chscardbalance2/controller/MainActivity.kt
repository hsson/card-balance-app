// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.*
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import se.creotec.chscardbalance2.BuildConfig
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.model.*
import se.creotec.chscardbalance2.receiver.CopyCardNumberReceiver
import se.creotec.chscardbalance2.service.AbstractBackendService
import se.creotec.chscardbalance2.service.BalanceService
import se.creotec.chscardbalance2.service.MenuService
import se.creotec.chscardbalance2.util.Util
import java.util.*

class MainActivity : AppCompatActivity(), FoodRestaurantFragment.OnListFragmentInteractionListener,
        OnCardDataChangedListener, OnMenuDataChangedListener, IModel.OnServiceFailedListener, NavigationView.OnNavigationItemSelectedListener, OnUserInfoChangedListener {
    private var parentView: View? = null
    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var quickChargeFAB: FloatingActionButton? = null

    private var cardOwnerName: TextView? = null
    private var cardNumber: TextView? = null

    private var drawerLayout: DrawerLayout? = null
    private var drawerView: NavigationView? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    private var showTextMenuButton: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parentView = findViewById(R.id.main_activity_parent)
        val global = application as GlobalState
        global.model.addCardDataListener(this)
        global.model.addMenuDataListener(this)
        global.model.addServiceFailedListener(this)
        global.model.addOnUserInfoChangedListener(this)

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

    override fun onUserInfoChanged(newUserInfo: String) {
        if (newUserInfo.isNotBlank()) {
            maybeUpdate(force = true)
        }
    }

    override fun serviceFailed(service: AbstractBackendService<*>, error: String) {
        runOnUiThread {
            swipeRefresh?.isRefreshing = false
            parentView?.let {
                if (service is BalanceService) {
                    Snackbar.make(it, R.string.error_card_failed, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_retry) { _ ->
                                val updateBalance = Intent(this, BalanceService::class.java)
                                updateBalance.action = Constants.ACTION_UPDATE_CARD
                                sendUpdateRequest(updateBalance)
                            }.show()
                } else if (service is MenuService) {
                    Snackbar.make(it, R.string.error_menu_failed, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_retry) { _ ->
                                val updateMenu = Intent(this, MenuService::class.java)
                                updateMenu.action = Constants.ACTION_UPDATE_MENU
                                sendUpdateRequest(updateMenu)
                            }.show()
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout?.closeDrawers()
        when (item.itemId) {
            R.id.drawer_menu_home -> {
                item.isChecked = true
            }
            R.id.drawer_menu_balance_history -> {
                item.isChecked = true
                // TODO: Go to history
            }
            R.id.drawer_menu_card_login -> {
                val cardLoginIntent = Intent(this, CardLoginActivity::class.java)
                startActivity(cardLoginIntent)
            }
            R.id.drawer_menu_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
            R.id.drawer_menu_about -> {
                val dialog = MaterialDialog.Builder(this)
                        .customView(R.layout.dialog_about, false)
                        .positiveText(R.string.action_close)
                        .neutralText(R.string.action_view_on_github)
                        .build()
                val versionText = dialog.customView?.findViewById(R.id.dialog_about_version) as TextView
                val gitHubButton = dialog.getActionButton(DialogAction.NEUTRAL)

                versionText.text = getString(R.string.dialog_about_version_text, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
                gitHubButton.setOnClickListener { _ ->
                    val webIntent = CustomTabsIntent.Builder()
                            .setToolbarColor(getColor(R.color.color_primary))
                            .build()
                    webIntent.launchUrl(this, Uri.parse(Constants.GITHUB_URL))
                }
                dialog.show()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            val inflater = menuInflater
            inflater.inflate(R.menu.action_menu, it)

            if (!showTextMenuButton) {
                it.findItem(R.id.action_item_charge).isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        drawerToggle?.let {
            if (it.onOptionsItemSelected(item)) {
                return true
            }
        }

        if (item?.itemId == R.id.action_item_charge) {
            launchChargeSite()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    // Sets up the appbar
    private fun setupAppBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        collapsingToolbarLayout = findViewById(R.id.toolbar_collapsing_layout)
        swipeRefresh = findViewById(R.id.swipe_refresh_container)
        cardOwnerName = findViewById(R.id.toolbar_card_name)
        cardNumber = findViewById(R.id.toolbar_card_number)
        appBarLayout = findViewById(R.id.app_bar_layout)
        drawerLayout = findViewById(R.id.main_drawer_layout)
        drawerView = findViewById(R.id.main_drawer_view)

        drawerView?.setNavigationItemSelectedListener(this)
        drawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.action_open,
                R.string.action_close) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(main_drawer_view)
                invalidateOptionsMenu()
                syncState()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(main_drawer_view)
                invalidateOptionsMenu()
                syncState()
            }
        }
        drawerToggle?.let {
            drawerLayout?.addDrawerListener(it)
        }

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        // Fade out name and card number when app bar is being collapsed
        appBarLayout?.let {
            it.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percentage = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
                val alpha = 1 - percentage * 3f
                cardOwnerName?.let { it.alpha = alpha }
                cardNumber?.let { it.alpha = alpha }
            }

            it.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                    if (state == AppBarStateChangeListener.State.COLLAPSED) {
                        showTextMenuButton = true
                        invalidateOptionsMenu()
                    }
                    else if (state == AppBarStateChangeListener.State.EXPANDED) {
                        showTextMenuButton = false
                        invalidateOptionsMenu()
                    }
                }
            })
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
        quickChargeFAB = findViewById<FloatingActionButton>(R.id.fab_charge_card)
        quickChargeFAB?.let {
            it.setOnClickListener {
                launchChargeSite()
            }
        }
    }

    private fun launchChargeSite() {
        val global = application as GlobalState

        val copyCardNumberIntent = Intent(this, CopyCardNumberReceiver::class.java)
        copyCardNumberIntent.action = Constants.ACTION_COPY_CARD_NUMBER
        copyCardNumberIntent.putExtra(Constants.EXTRAS_CARD_NUMBER_KEY, global.model.cardData.cardNumber)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, copyCardNumberIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val icon = BitmapFactory.decodeResource(resources, R.drawable.chrome_icon_copy)

        val webIntent = CustomTabsIntent.Builder()
                .setToolbarColor(getColor(R.color.color_primary))
                .addMenuItem(getString(R.string.action_copy_card_number), pendingIntent)
                .setActionButton(icon, getString(R.string.action_copy_card_number), pendingIntent, true)
                .build()
        webIntent.launchUrl(this, Uri.parse(global.model.quickChargeURL))
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
                        .setAction(R.string.action_connect) { _ ->
                            startActivity(Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
                        }.show()
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
        private const val HOUR: Long = 1000 * 60 * 60 // 1 hour in ms

        private val HALF_HOUR_AGO: Long = HOUR/2
            get() = System.currentTimeMillis() - field
        private val THREE_HOURS_AGO:Long = HOUR * 3
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
