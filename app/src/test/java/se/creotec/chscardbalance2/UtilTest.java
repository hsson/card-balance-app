package se.creotec.chscardbalance2;

import org.junit.Test;

import se.creotec.chscardbalance2.util.Util;

import static org.junit.Assert.*;

/**
 * Unit tests for the Util package
 */
public class UtilTest {

    @Test
    public void formatter_isCorrect() throws Exception {
        assertEquals("1111 2222 3333 4444", Util.formatCardNumber("1111222233334444"));
        assertEquals("11111", Util.formatCardNumber("11111"));
        assertEquals("1122 3344", Util.formatCardNumber("11223344"));
        assertEquals(null, Util.formatCardNumber(null));
        assertEquals("", Util.formatCardNumber(""));
    }
}