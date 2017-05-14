// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.service;

public final class BackendFetchException extends Exception {
    public BackendFetchException() {
        super();
    }
    public BackendFetchException(String message) {
        super(message);
    }
}
