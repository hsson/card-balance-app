// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service

import android.content.Intent
import android.util.Log
import com.google.gson.reflect.TypeToken
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.model.BackendResponse
import se.creotec.chscardbalance2.model.CardData

class BalanceService : AbstractBackendService<CardData>(BalanceService::class.java.name) {
    private val LOG_TAG = BalanceService::class.java.name
    override val responseType = object : TypeToken<BackendResponse<CardData>>() {}.type!!

    override fun onHandleIntent(intent: Intent?) {
        Log.i(LOG_TAG, "Received intent")
        if (intent == null || intent.action == null || !hasInternet()) {
            return
        } else if (intent.action == Constants.ACTION_UPDATE_CARD) {
            val global = application as GlobalState
            val cardNumber = global.model.cardData.cardNumber
            cardNumber?.let {
                updateCard(it, global)
            }
        }
    }

    private fun updateCard(cardNumber: String, global: GlobalState) {
        try {
            val response = getBackendData(Constants.ENDPOINT_BALANCE, cardNumber)
            if (response.isSuccess) {
                global.model.cardData = response.data ?: CardData()
                global.model.cardLastTimeUpdated = System.currentTimeMillis()
                global.saveCardData()
                Log.i(LOG_TAG, "Got response: " + response.toString())
            }
        } catch (e: BackendFetchException) {
            Log.e(LOG_TAG, e.message)
            global.model.notifyServiceFailed(this, e.message ?: "")
        }
    }

    override fun isVariableValid(variable: String): Boolean {
        if (variable.length != Constants.CARD_NUMBER_LENGTH) {
            return false
        }
        return variable.matches("[0-9]+".toRegex())
    }
}
