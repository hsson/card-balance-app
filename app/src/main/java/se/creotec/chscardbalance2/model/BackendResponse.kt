// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model

import com.google.gson.annotations.SerializedName

class BackendResponse<T> {

    @SerializedName("success")
    var isSuccess: Boolean = false
    @SerializedName("error")
    var errorMessage: String = UNKNOWN_ERROR
    @SerializedName("data")
    var data: T? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as BackendResponse<*>
        if (isSuccess != that.isSuccess || errorMessage != that.errorMessage) return false
        return if (data != null) data == that.data else that.data == null
    }

    override fun hashCode(): Int {
        var result = if (isSuccess) 1 else 0
        result = 37 * result + errorMessage.hashCode()
        result = 37 * result + (data?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "BackendResponse{" +
                "success=" + isSuccess +
                ", errorMessage='" + errorMessage + '\'' +
                ", data=" + data +
                '}'
    }

    companion object {
        val UNKNOWN_ERROR = "Unknown error"
    }
}
