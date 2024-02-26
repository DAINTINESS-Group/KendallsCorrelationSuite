package listBasedKendallAlgorithms;

import org.junit.Test;
import static org.junit.Assert.*;

public class ListBasedKendallMethodsServiceTest {

    @Test
    public void testGetMethod() {
        ListBasedKendallMethodsService service = new ListBasedKendallMethodsService();

        // Test BROPHY method
        IListBasedKendallCalculator brophyCalculator = service.getMethod("Brophy");
        assertNotNull(brophyCalculator);
        assertTrue(brophyCalculator instanceof BrophyKendallCalculator);

        // Test BRUTEFORCE method
        IListBasedKendallCalculator bruteForceCalculator = service.getMethod("BruteForce");
        assertNotNull(bruteForceCalculator);
        assertTrue(bruteForceCalculator instanceof BruteForceNoTiesKendallCalculator);

        // Test APACHE COMMONS method
        IListBasedKendallCalculator apacheKendallCalculator = service.getMethod("Apache kendall");
        assertNotNull(apacheKendallCalculator);
        assertTrue(apacheKendallCalculator instanceof ApacheCommonsKendall);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidMethod() {
        ListBasedKendallMethodsService service = new ListBasedKendallMethodsService();
        service.getMethod("InvalidMethod");
    }
}
