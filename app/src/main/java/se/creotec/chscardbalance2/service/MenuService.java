// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.model.BackendResponse;
import se.creotec.chscardbalance2.model.MenuData;

public class MenuService extends BackendService<MenuData> {

    private static final String LOG_TAG = MenuService.class.getName();
    private final Type responseType = new TypeToken<BackendResponse<MenuData>>() {}.getType();


    public MenuService() {
        super(MenuService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(LOG_TAG, "Received intent");
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(Constants.ACTION_UPDATE_MENU)) {
            try {
                BackendResponse<MenuData> response = getBackendData(Constants.ENDPOINT_MENU, Constants.ENDPOINT_MENU_LANG_EN);
                Log.i(LOG_TAG, "Got response: " + response.getData().toString());
            } catch (BackendFetchException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
    }

    @Override
    protected void validateVariable(String variable) throws BackendFetchException {
        switch (variable) {
            case Constants.ENDPOINT_MENU_LANG_EN:
            case Constants.ENDPOINT_MENU_LANG_SV:
                break;
            default:
                throw new BackendFetchException("Unsupported language: " + variable);
        }
    }

    @Override
    protected Type getResponseType() {
        return responseType;
    }
}
