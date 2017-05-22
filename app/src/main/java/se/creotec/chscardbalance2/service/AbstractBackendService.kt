// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service

import android.app.IntentService
import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import se.creotec.chscardbalance2.BuildConfig
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.model.BackendResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

abstract class AbstractBackendService<T>(name: String) : IntentService(name) {

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
    @Throws(BackendFetchException::class)
    protected fun getBackendData(endpoint: String, variable: String): BackendResponse<T> {
        if (variable != "" && !isVariableValid(variable)) {
            throw BackendFetchException("Endpoint variable not valid")
        }

        val conn: HttpURLConnection
        val responseCode: Int
        try {
            conn = prepareConnection(endpoint, variable)
            conn.connect()
            responseCode = conn.responseCode

        } catch (e: IOException) {
            throw BackendFetchException("Unable to create connection to backend")
        }

        when (responseCode) {
            HttpURLConnection.HTTP_OK -> {
                val response = readResponse(conn)
                return parseResponse(response)
            }
            HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                conn.disconnect()
                throw BackendFetchException("The backend encountered an error")
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                conn.disconnect()
                throw BackendFetchException("The requested endpoint was not found")
            }
            else -> {
                conn.disconnect()
                throw BackendFetchException("Backend responded with unknown error")
            }
        }
    }

    @Throws(BackendFetchException::class)
    private fun readResponse(connection: HttpURLConnection): String {
        try {
            connection.inputStream.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val builder = StringBuilder()

                var line: String? = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }
                return builder.toString().trim()
            }
        } catch (e: IOException) {
            throw BackendFetchException("Error when reading stream from backend")
        }

    }

    @Throws(IOException::class)
    private fun prepareConnection(endpoint: String, variable: String): HttpURLConnection {
        val formattedURL = URL(BuildConfig.BACKEND_URL + endpoint + "/" + variable)
        val connection = formattedURL.openConnection() as HttpURLConnection
        connection.readTimeout = Constants.ENDPOINT_TIMEOUT
        connection.connectTimeout = Constants.ENDPOINT_TIMEOUT
        connection.doInput = true
        connection.requestMethod = "GET"
        return connection
    }

    @Throws(BackendFetchException::class)
    private fun parseResponse(rawResponse: String): BackendResponse<T> {
        val response = Gson().fromJson<BackendResponse<T>>(rawResponse, responseType)
        if (!response.isSuccess) {
            throw BackendFetchException(response.errorMessage)
        }
        return response
    }

    protected fun hasInternet(): Boolean {
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    /**
     * Checks if the passed variable is valid. The validation is performed
     * before any attempt is made to connect to the endpoint. If a validation doesn't pass,
     * the request will not be performed.

     * @param variable The variable to validate
     */

    protected abstract fun isVariableValid(variable: String): Boolean

    /**
     * A method for getting the type of the expected response. The method is used when
     * the parsing from JSON to a Java Object is performed.

     * @return The type of the expected backend response
     */
    protected abstract val responseType: Type
}
