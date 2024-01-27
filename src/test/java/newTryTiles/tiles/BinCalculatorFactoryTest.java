package newTryTiles.tiles;

import org.junit.Test;

import newTryTiles.algo.BinCalculatorFactory;
import newTryTiles.algo.SquareRootBinCalculator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BinCalculatorFactoryTest {

    @Test
    public void testCreateSquareRootBinCalculator() {
        BinCalculatorFactory factory = new BinCalculatorFactory();

        SquareRootBinCalculator calculator = factory
                .createBinCalculator(BinCalculatorFactory.BinCalculatorMethods.SQUARE_ROOT);

        assertNotNull(calculator);
        assertTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUnknownBinCalculator() {
        BinCalculatorFactory factory = new BinCalculatorFactory();

        factory.createBinCalculator(null);
    }

}

