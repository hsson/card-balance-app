// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.controller

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.util.Util

class CardLoginActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CookieManager.getInstance().setAcceptCookie(true)
        val global = application as GlobalState
        val webView = WebView(application)
        setContentView(webView)
        val targetURL = global.model.quickChargeURL

        webView.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        webView.webViewClient = LoginWebClient(Uri.parse(targetURL))
        webView.loadUrl(targetURL)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.action_menu_login, it)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_item_finished) {
            // User is finished, go back
            // TODO: GO back to main activity and force a card update
            // TODO: return true
        } else if (item?.itemId == R.id.action_item_copy_number) {
            val global = application as GlobalState


            val cardNumber = global.model.cardData.cardNumber
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("cardNumber", cardNumber)
            clipboard.primaryClip = clipData
            val toastString = getString(R.string.card_number_copied, Util.formatCardNumber(cardNumber))
            Toast.makeText(this, toastString, Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    inner class LoginWebClient(private val hostURL: Uri) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            request?.let { req ->
                if (req.url.host.contains("chalmers") || req.url.host == hostURL.host) {
                    // This URL is within the card website, allow it
                    return false
                }
                Intent(Intent.ACTION_VIEW, req.url).apply {
                    startActivity(this)
                }
                return true
            } ?: run {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            CookieManager.getInstance().getCookie(url)?.let { cookieString ->
                cookieString.split(";").forEach {  cookie ->
                    if (cookie.toLowerCase().contains(Constants.COOKIE_USERINFO.toLowerCase())) {
                        cookie.split("=").getOrNull(1)?.let { userinfo ->
                            // Found user info
                            val global = application as GlobalState
                            global.model.userInfo = userinfo
                            global.saveUserInfoData()
                        }
                    }
                }
            }
        }
    }
}
