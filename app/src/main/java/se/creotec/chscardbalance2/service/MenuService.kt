// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service

import android.content.Intent
import android.util.Log

import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type

import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.GlobalState
import se.creotec.chscardbalance2.model.BackendResponse
import se.creotec.chscardbalance2.model.MenuData


class MenuService : AbstractBackendService<MenuData>(MenuService::class.java.name) {
    private val responseType = object : TypeToken<BackendResponse<MenuData>>() {}.type
    private val LOG_TAG = MenuService::class.java.name

    override fun onHandleIntent(intent: Intent?) {
        Log.i(LOG_TAG, "Received intent")
        if (intent == null || intent.action == null) {
            return
        }
        if (intent.action == Constants.ACTION_UPDATE_MENU) {
            try {
                val global = application as GlobalState
                val language = global.model.preferredMenuLanguage
                val response = getBackendData(Constants.ENDPOINT_MENU, language)
                global.model.menuData = response.data
                global.saveMenuData()
                Log.i(LOG_TAG, "Got response: " + response.data.toString())
            } catch (e: BackendFetchException) {
                Log.e(LOG_TAG, e.message)
            }

        }
    }

    @Throws(BackendFetchException::class)
    override fun validateVariable(variable: String) {
        when (variable) {
            Constants.ENDPOINT_MENU_LANG_EN, Constants.ENDPOINT_MENU_LANG_SV -> {
            }
            else -> throw BackendFetchException("Unsupported language: " + variable)
        }
    }

    override fun getResponseType(): Type {
        return responseType
    }
}
