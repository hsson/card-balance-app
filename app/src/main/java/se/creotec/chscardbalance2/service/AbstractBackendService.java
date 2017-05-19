// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service;

import android.app.IntentService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

public abstract class AbstractBackendService<T> extends IntentService {

    public AbstractBackendService(String name) {
        super(name);
    }

    /**
     * HTTP GETs data from the backend given some endpoint and some optional
     * variable to that endpoint. The data is retrieved by performing an
     * HTTP GET request to <backend-url><endpoint>/<variable>
     *
     * @param endpoint The endpoint to GET from, starting with a '/'
     * @param variable An optional variable to pass to the endpoint
     * @return Returns the parsed response from the backend
     * @throws BackendFetchException If some error occurred when making the request or parsing the response
     */
    protected BackendResponse<T> getBackendData(@NonNull String endpoint, @Nullable String variable) throws BackendFetchException {
        if (variable != null) {
            validateVariable(variable);
        }
        if (variable == null) {
            variable = "";
        }
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
        connection.setReadTimeout(Constants.INSTANCE.getENDPOINT_TIMEOUT());
        connection.setConnectTimeout(Constants.INSTANCE.getENDPOINT_TIMEOUT());
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        return connection;
    }

    private BackendResponse<T> parseResponse(String rawResponse) throws BackendFetchException {
        BackendResponse<T> response =  new Gson().fromJson(rawResponse, getResponseType());
        if (!response.isSuccess()) {
            throw new BackendFetchException(response.getErrorMessage());
        }
        return response;
    }

    /**
     * Performs validation on the passed variable. The validation is performed
     * before any attempt is made to connect to the endpoint. If a validation doesn't pass,
     * the BackendFetchException should be thrown to cancel the request.
     *
     * @param variable The variable to validate
     * @throws BackendFetchException If some validation failed
     */
    protected abstract void validateVariable(String variable) throws BackendFetchException;

    /**
     * A method for getting the type of the expected response. The method is used when
     * the parsing from JSON to a Java Object is performed.
     *
     * @return The type of the expected backend response
     */
    protected abstract Type getResponseType();
}
