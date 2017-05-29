// Copyright (c) 2017 Alexander Håkansson
//
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT
package se.creotec.chscardbalance2.util;

import java.util.Calendar;
import java.util.Date;

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

    /**
     * Capitalize the first letter of all words in the given string
     * @param in The string to format
     * @return The input string with all first letters capitalized
     */
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

    /**
     * Determine if the time is currently between two given hours and minutes.
     * @param from A time of the day on the format where i.e. 800 is 8.00 am, and 2330 is 11.30 pm
     * @param to A time of the day on the format where i.e. 800 is 8.00 am, and 2330 is 11.30 pm
     * @return A boolean indicating if the time of the day is between the given interval
     */
    public static boolean isBetweenHours(int from, int to) {
        return isBetweenHours(new Date(), from, to);
    }

    /**
     * Determine if the given time is between two given hours and minutes.
     * @param date The date to check if between interval.
     * @param from A time of the day on the format where i.e. 800 is 8.00 am, and 2330 is 11.30 pm
     * @param to A time of the day on the format where i.e. 800 is 8.00 am, and 2330 is 11.30 pm
     * @return A boolean indicating if the time of the day is between the given interval
     */
    public static boolean isBetweenHours(Date date, int from, int to) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        return to > from && t >= from && t <= to || to < from && (t >= from || t <= to);
    }
}
