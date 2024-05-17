package listBasedKendallAlgorithms;

import org.junit.Test;
import static org.junit.Assert.*;

import listBasedKendallAlgorithms.IListBasedKendallFactory.KendallCalculatorMethods;

public class IListBasedKendallFactoryTest {

    @Test
    public void testGetMethod() {
        IListBasedKendallFactory service = new IListBasedKendallFactory();

        // Test BROPHY method
        IListBasedKendallCalculator brophyCalculator = service.createKendallCalculator(KendallCalculatorMethods.BROPHY, null);
        assertNotNull(brophyCalculator);
        assertTrue(brophyCalculator instanceof BrophyKendallCalculator);

        // Test BRUTEFORCE method
        IListBasedKendallCalculator bruteForceCalculator = service.createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE, null);
        assertNotNull(bruteForceCalculator);
        assertTrue(bruteForceCalculator instanceof BruteForceNoTiesKendallCalculator);

        // Test APACHE COMMONS method
        IListBasedKendallCalculator apacheKendallCalculator = service.createKendallCalculator(KendallCalculatorMethods.APACHE, null);
        assertNotNull(apacheKendallCalculator);
        assertTrue(apacheKendallCalculator instanceof ApacheCommonsKendall);
    }

    //@Test(expected = IllegalArgumentException.class)
    @Test(expected = NullPointerException.class)
    public void testGetMethodWithInvalidMethod() {
        IListBasedKendallFactory service = new IListBasedKendallFactory();
        service.createKendallCalculator(null, null);
    }
}
