// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.receiver

import android.content.*
import android.widget.Toast
import se.creotec.chscardbalance2.Constants
import se.creotec.chscardbalance2.R
import se.creotec.chscardbalance2.util.Util

class CopyCardNumberReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action == Constants.ACTION_COPY_CARD_NUMBER
                && intent.hasExtra(Constants.EXTRAS_CARD_NUMBER_KEY)) {
            val cardNumber = intent.extras.getString(Constants.EXTRAS_CARD_NUMBER_KEY)
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("cardNumber", cardNumber)
            clipboard.primaryClip = clipData
            val toastString = context.getString(R.string.card_number_copied, Util.formatCardNumber(cardNumber))
            Toast.makeText(context, toastString, Toast.LENGTH_LONG).show()
        }
    }
}