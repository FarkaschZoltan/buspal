package hu.kristol.buspal;

import org.junit.Test;

import hu.farkasch.buspalbackend.datastructures.Coordinates;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    @Test
    public void isInsideRadiusTestTrue() {
        assertTrue(Coordinates.isInsideRadius(new Coordinates(47.530207,19.007196), new Coordinates(47.530185, 19.008280), 500));
    }

    @Test
    public void isInsideRadiusTestFalse() {
        assertFalse(Coordinates.isInsideRadius(new Coordinates(47.530207,19.007196), new Coordinates(47.530185, 20.008280), 500));
    }

    @Test
    public void checkDistanceCalculation(){
        Coordinates p1 = new Coordinates(47.611002, 19.103699); //Budapest's northest point
        Coordinates p2 = new Coordinates(47.376773, 19.101796); //Budapest's southest point
        int distance = (int)Math.floor(Coordinates.getDistance(p1, p2) / 1000) * 1000;
        System.out.println(distance);
        assertEquals( 26000, distance);
    }
}