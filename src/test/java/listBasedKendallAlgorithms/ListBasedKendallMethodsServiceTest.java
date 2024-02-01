package listBasedKendallAlgorithms;

import static org.junit.Assert.*;
import org.junit.Test;

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

        // Test invalid method
        IListBasedKendallCalculator invalidCalculator = service.getMethod("InvalidMethod");
        assertNull(invalidCalculator);
    }
}
