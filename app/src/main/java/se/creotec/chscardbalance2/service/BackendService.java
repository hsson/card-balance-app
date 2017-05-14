// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service;

import android.app.IntentService;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import se.creotec.chscardbalance2.BuildConfig;
import se.creotec.chscardbalance2.Constants;
import se.creotec.chscardbalance2.model.BackendResponse;

public abstract class BackendService<T> extends IntentService {

    public BackendService(String name) {
        super(name);
    }

    protected BackendResponse<T> getBackendData(String endpoint, String variable) throws BackendFetchException {
        validateVariable(variable);
        HttpURLConnection conn;
        int responseCode;
        try {
            conn = prepareConnection(endpoint, variable);
            conn.connect();
            responseCode = conn.getResponseCode();

        } catch (IOException e) {
            throw new BackendFetchException("Unable to create connection to backend");
        }

        switch (responseCode) {
            case HttpURLConnection.HTTP_OK: // 200
                String response = readResponse(conn);
                return parseResponse(response);
            case HttpURLConnection.HTTP_INTERNAL_ERROR: // 500
                throw new BackendFetchException("The backend encountered an error");
            case HttpURLConnection.HTTP_NOT_FOUND: // 404
                throw new BackendFetchException("The requested endpoint was not found");
            default:
                throw new BackendFetchException("Backend responded with unknown error");
        }
    }

    private String readResponse(HttpURLConnection connection) throws BackendFetchException {
        try (InputStream inputStream = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString().trim();
        } catch (IOException e) {
            throw new BackendFetchException("Error when reading stream from backend");
        }
    }

    private HttpURLConnection prepareConnection(String endpoint, String variable) throws IOException {
        URL formattedURL = new URL(BuildConfig.BACKEND_URL + endpoint + "/" + variable);
        HttpURLConnection connection = (HttpURLConnection) formattedURL.openConnection();
        connection.setReadTimeout(Constants.ENDPOINT_TIMEOUT);
        connection.setConnectTimeout(Constants.ENDPOINT_TIMEOUT);
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        return connection;
    }

    protected BackendResponse<T> parseResponse(String rawResponse) throws BackendFetchException {
        BackendResponse<T> response =  new Gson().fromJson(rawResponse, getResponseType());
        if (!response.isSuccess()) {
            throw new BackendFetchException(response.getErrorMessage());
        }
        return response;
    }

    protected abstract void validateVariable(String variable) throws BackendFetchException;
    //protected abstract BackendResponse<T> parseResponse(String rawResponse) throws BackendFetchException;
    protected abstract Type getResponseType();
}
