package se.creotec.chscardbalance2;

import org.junit.Test;

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
}