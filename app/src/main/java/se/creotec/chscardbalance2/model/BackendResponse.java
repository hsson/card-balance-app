// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.model;

public class BackendResponse<T> {
    public static final String UNKNOWN_ERROR = "Unknown error";

    private boolean success;
    private String errorMessage;
    private T data;

    public BackendResponse() {
        this.success = false;
        this.errorMessage = UNKNOWN_ERROR;
        this.data = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BackendResponse<?> that = (BackendResponse<?>) o;

        if (success != that.success) return false;
        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null)
            return false;
        return data != null ? data.equals(that.data) : that.data == null;

    }

    @Override
    public int hashCode() {
        int result = (success ? 1 : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BackendResponse{" +
                "success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
