package se.creotec.chscardbalance2;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import se.creotec.chscardbalance2.util.Util;

import static org.junit.Assert.*;

/**
 * Unit tests for the Util package
 */
public class UtilTest {

    @Test
    public void cardNumber_formatter_isCorrect() throws Exception {
        assertEquals("1111 2222 3333 4444", Util.formatCardNumber("1111222233334444"));
        assertEquals("11111", Util.formatCardNumber("11111"));
        assertEquals("1122 3344", Util.formatCardNumber("11223344"));
        assertEquals(null, Util.formatCardNumber(null));
        assertEquals("", Util.formatCardNumber(""));
    }

    @Test
    public void capFirstInWords_formatter_isCorrect() throws Exception {
        assertEquals("Hello World", Util.capitalizeAllWords("hello world"));
        assertEquals("Hello World", Util.capitalizeAllWords("Hello world"));
        assertEquals("Hello World", Util.capitalizeAllWords("hello World"));
        assertEquals("", Util.capitalizeAllWords(""));
        assertEquals(null, Util.capitalizeAllWords(null));
        assertEquals("Helloworld", Util.capitalizeAllWords("helloworld"));
    }

    @Test
    public void timeBetweenHours_isCorrect() throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, 8);
        assertEquals(true, Util.isBetweenHours(c.getTime(), 700,900));
        assertEquals(true, Util.isBetweenHours(c.getTime(), 800,900));
        assertEquals(false, Util.isBetweenHours(c.getTime(), 700, 800));
        assertEquals(false, Util.isBetweenHours(c.getTime(), 900, 1000));
        assertEquals(false, Util.isBetweenHours(c.getTime(), 1900, 2100));

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 30);
        assertEquals(true, Util.isBetweenHours(c.getTime(), 2300, 2400));
        assertEquals(true, Util.isBetweenHours(c.getTime(), 2300, 0));
        assertEquals(true, Util.isBetweenHours(c.getTime(), 2329,2331));
        assertEquals(false, Util.isBetweenHours(c.getTime(), 2300, 2329));

    }
}