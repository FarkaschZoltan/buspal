package hu.kristol.buspal;

import org.junit.Test;

import hu.farkasch.buspalbackend.datastructures.Coordinates;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isInsideRadiusTestTrue() {
        assertTrue(Coordinates.isInsideRadius(new Coordinates(47.530207,19.007196), new Coordinates(47.530185, 19.008280), 500));
    }

    @Test
    public void isInsideRadiusTestFalse() {
        assertFalse(Coordinates.isInsideRadius(new Coordinates(47.530207,19.007196), new Coordinates(47.530185, 20.008280), 500));
    }
}