// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.util

import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher

// TODO: Make perfect
class CardNumberMask : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        if (s == null) {
            return
        }
        // Remove spacing char
        if (s.isNotEmpty() && s.length % 5 == 0) {
            val c = s[s.length - 1]
            if (space == c) {
                s.delete(s.length - 1, s.length)
            }
        }
        // Insert char where needed.
        if (s.isNotEmpty() && (s.length % 5) == 0) {
            val c: Char = s[s.length - 1]
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(s.toString(), space.toString()).size <= 3) {
                val filters = s.filters
                s.filters = arrayOf<InputFilter>()
                s.insert(s.length - 1, space.toString())
                s.filters = filters
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    companion object {
        private val space: Char = ' '
    }
}