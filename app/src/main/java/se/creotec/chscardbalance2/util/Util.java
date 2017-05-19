// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.util;

public final class Util {
    private Util() {}

    /**
     * Formats the card number to insert spaces between every 4 digits.
     * E.g. 111122223333444 --> 1111 2222 3333 4444
     *
     * The card number must have a length that is a multiple of 4.
     *
     * @param cardNumber The card number to format, where the length is a multiple of 4
     * @return The cardNumber formatted, if length was correct. If length was incorrect, the same number is returned again
     */
    public static String formatCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length()%4 != 0) {
            return cardNumber;
        }
        StringBuilder sb = new StringBuilder(cardNumber);

        for (int i = 4; i < sb.length(); i += 5) {
            sb.insert(i, ' ');
        }

        return sb.toString();
    }

    public static String capitalizeAllWords(String in) {
        if (in == null  || in.equals("")) {
            return in;
        }

        StringBuilder res = new StringBuilder();

        String[] strArr = in.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);

            res.append(str).append(" ");
        }
        return res.toString().trim();
    }
}
