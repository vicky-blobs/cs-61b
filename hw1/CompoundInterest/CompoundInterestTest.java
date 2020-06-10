import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(CompoundInterest.numYears(2021), 1);
        assertEquals(CompoundInterest.numYears(2030), 10);
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValue(10, 12, 2022), 12.544, tolerance);
        assertEquals(CompoundInterest.futureValue(20, -12, 2023), 13.629, tolerance);
        assertEquals(CompoundInterest.futureValue(10, -10, 2021), 9.000, tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.futureValueReal(10, 12, 2022, 3), 11.8026496, tolerance);
        assertEquals(CompoundInterest.futureValueReal(20, -12, 2023, 4), 12.058, tolerance);
        assertEquals(CompoundInterest.futureValueReal(10, -10, 2021, 2), 8.82, tolerance);
    }

    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavings(5000, 2022, 10), 16550, tolerance);
        assertEquals(CompoundInterest.totalSavings(1000, 2023, -10), 3439, tolerance);
        assertEquals(CompoundInterest.totalSavings(3000, 2021, 15), 6450, tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(CompoundInterest.totalSavingsReal(5000, 2022, 10, 3), 15571.9, tolerance);
        assertEquals(CompoundInterest.totalSavingsReal(1000, 2023, -10, 5), 2948.51, tolerance);
        assertEquals(CompoundInterest.totalSavingsReal(3000, 2021, 15, 10), 5805, tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
