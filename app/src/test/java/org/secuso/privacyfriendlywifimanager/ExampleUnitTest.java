package org.secuso.privacyfriendlywifimanager;

import junit.framework.Assert;

import org.junit.Test;
import org.secuso.privacyfriendlywifimanager.logic.types.PrimitiveCellInfo;
import org.secuso.privacyfriendlywifimanager.logic.types.PrimitiveCellInfoTreeSet;

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

    @Test
    public void test_time() {
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 0, 0));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 0, 1));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 0, 2));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 0, 3));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 0, 4));

        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 1, 0));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 1, 1));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 1, 2));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 1, 3));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 1, 4));

        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 2, 0));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 2, 1));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 2, 2));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 2, 3));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 2, 4));

        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 3, 0));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 3, 1));
        Assert.assertEquals(true, time_tester(1, 3, 1, 3, 3, 2));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 3, 3));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 3, 4));

        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 4, 0));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 4, 1));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 4, 2));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 4, 3));
        Assert.assertEquals(false, time_tester(1, 3, 1, 3, 4, 4));

        //

        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 0, 0));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 0, 1));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 0, 2));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 0, 3));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 0, 4));

        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 1, 0));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 1, 1));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 1, 2));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 1, 3));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 1, 4));

        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 2, 0));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 2, 1));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 2, 2));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 2, 3));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 2, 4));

        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 3, 0));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 3, 1));
        Assert.assertEquals(false, time_tester(3, 1, 3, 1, 3, 2));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 3, 3));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 3, 4));

        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 4, 0));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 4, 1));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 4, 2));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 4, 3));
        Assert.assertEquals(true, time_tester(3, 1, 3, 1, 4, 4));

        Assert.assertEquals(false, time_tester(23, 0, 0, 0, 1, 0));
        Assert.assertEquals(false, time_tester(23, 0, 0, 0, 1, 0));
        Assert.assertEquals(true, time_tester(23, 2, 2, 0, 1, 1)); // 23:02 - 02:00, 01:01
        Assert.assertEquals(true, time_tester(0, 0, 50, 20, 0, 30));

        Assert.assertEquals(false, time_tester(1, 3, 3, 1, 0, 30));
        Assert.assertEquals(true, time_tester(1, 3, 3, 1, 2, 30));
        Assert.assertEquals(false, time_tester(0, 0, 0, 0, 0, 0));
        Assert.assertEquals(false, time_tester(0, 0, 0, 0, 5, 0));
        Assert.assertEquals(true, time_tester(0, 0, 50, 20, 1, 0));

        Assert.assertEquals(true, time_tester(0, 2, 50, 20, 1, 51));
    }

    private boolean time_tester(int start_hour, int end_hour, int start_minute, int end_minute, int current_hour, int current_minute) {
        if (start_hour < end_hour) {

            if (current_hour == start_hour) {
                return current_minute >= start_minute;
            }

            if (current_hour == end_hour) {
                return current_minute < end_minute;
            }

            return current_hour > start_hour && current_hour < end_hour;
        } else if (start_hour > end_hour) {
            if (current_hour == start_hour) {
                return current_minute >= start_minute;
            }

            if (current_hour == end_hour) {
                return current_minute < end_minute;
            }

            return current_hour > start_hour || current_hour < end_hour;
        } else {
            if (start_minute == end_minute) {
                return false;
            }
            if (current_hour == start_hour) {
                if (start_minute < end_minute) {
                    return current_minute >= start_minute && current_minute < end_minute;
                } else if (start_minute > end_minute) {
                    return current_minute <= start_minute || current_minute > end_minute;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }
}