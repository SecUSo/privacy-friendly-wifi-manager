package secuso.org.privacyfriendlywifi;

import org.junit.Test;

import secuso.org.privacyfriendlywifi.logic.types.PrimitiveCellInfo;
import secuso.org.privacyfriendlywifi.logic.types.PrimitiveCellInfoTreeSet;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_treeset() {
        PrimitiveCellInfoTreeSet cells = new PrimitiveCellInfoTreeSet();
        cells.add(new PrimitiveCellInfo(123, 5));
        cells.add(new PrimitiveCellInfo(123, 6));
        cells.add(new PrimitiveCellInfo(123, 3));
        cells.add(new PrimitiveCellInfo(123, -1));

        for (PrimitiveCellInfo cell : cells) {
            System.out.println(cell.getSignalStrength());
        }
    }
}