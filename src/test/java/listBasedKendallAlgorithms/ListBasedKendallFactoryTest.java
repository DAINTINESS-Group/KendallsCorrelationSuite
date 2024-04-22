package listBasedKendallAlgorithms;

import org.junit.Test;
import static org.junit.Assert.*;

public class ListBasedKendallFactoryTest {

    @Test
    public void testGetMethod() {
        ListBasedKendallFactory service = new ListBasedKendallFactory();

        // Test BROPHY method
        IListBasedKendallCalculator brophyCalculator = service.createKendallCalculatorByString("Brophy");
        assertNotNull(brophyCalculator);
        assertTrue(brophyCalculator instanceof BrophyKendallCalculator);

        // Test BRUTEFORCE method
        IListBasedKendallCalculator bruteForceCalculator = service.createKendallCalculatorByString("BruteForce");
        assertNotNull(bruteForceCalculator);
        assertTrue(bruteForceCalculator instanceof BruteForceNoTiesKendallCalculator);

        // Test APACHE COMMONS method
        IListBasedKendallCalculator apacheKendallCalculator = service.createKendallCalculatorByString("Apache kendall");
        assertNotNull(apacheKendallCalculator);
        assertTrue(apacheKendallCalculator instanceof ApacheCommonsKendall);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidMethod() {
        ListBasedKendallFactory service = new ListBasedKendallFactory();
        service.createKendallCalculatorByString("InvalidMethod");
    }
}
