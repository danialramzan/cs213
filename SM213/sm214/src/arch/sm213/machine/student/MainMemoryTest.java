package arch.sm213.machine.student;

import machine.AbstractMainMemory;
import org.junit.*;


import static org.junit.Assert.*;
import machine.AbstractMainMemory.InvalidAddressException;

/**
 * Main Memory of Simple CPU.
 *
 * Provides an abstraction of main memory (DRAM).
 */

public class MainMemoryTest {

    private MainMemory testMainMemory;

    @Before
    public void BeforeEach() {
        testMainMemory = new MainMemory(16);
    }

    @Test
    public void testIsAccessAligned() {

        // check true case
        assertTrue(testMainMemory.isAccessAligned(8, 4));

        //check false case
        assertFalse(testMainMemory.isAccessAligned(8, 3));

        //check boundary true case
        assertTrue(testMainMemory.isAccessAligned(8, 8));

        // check boundary true case
        assertTrue(testMainMemory.isAccessAligned(8, 1));
    }

    @Test
    public void testBytesToInteger() {

        // boundary case (min)
        assertEquals(0x00000000, testMainMemory.bytesToInteger(((byte) 0x00), ((byte) 0x00), ((byte) 0x00), ((byte) 0x00)));

        // non zero testto integer
        assertEquals(0x10203040, testMainMemory.bytesToInteger(((byte) 0x10), ((byte) 0x20), ((byte) 0x30), ((byte) 0x40)));

        // test to ensure inappropriate sign extension doesn't occur
        assertEquals(0x8090a0b0, testMainMemory.bytesToInteger(((byte) 0x80), ((byte) 0x90), ((byte) 0xa0), ((byte) 0xb0)));

        // boundary case (max)
        assertEquals(0xffffffff, testMainMemory.bytesToInteger(((byte) 0xff), ((byte) 0xff), ((byte) 0xff), ((byte) 0xff)));
    }

    @Test
    public void testIntegerToBytes() {


        // boundary case (min)
        assertArrayEquals(byteMaker(((byte) 0x00), ((byte) 0x00), ((byte) 0x00), ((byte) 0x00)), testMainMemory.integerToBytes(0x00000000));

        // non zero test to integer
        assertArrayEquals(byteMaker(((byte) 0x10), ((byte) 0x20), ((byte) 0x30), ((byte) 0x40)), testMainMemory.integerToBytes(0x10203040));

        // test to ensure inappropriate sign extension doesn't occur
        assertArrayEquals(byteMaker(((byte) 0x80), ((byte) 0x90), ((byte) 0xa0), ((byte) 0xb0)), testMainMemory.integerToBytes(0x8090a0b0));

        // boundary case (max)
        assertArrayEquals(byteMaker(((byte) 0xff), ((byte) 0xff), ((byte) 0xff), ((byte) 0xff)), testMainMemory.integerToBytes(0xffffffff));
    }

    public byte[] byteMaker(byte first, byte second, byte third, byte fourth) {
        byte[] arr = new byte[4];
        arr[0] = first;
        arr[1] = second;
        arr[2] = third;
        arr[3] = fourth;
        return arr;
    }

    @Test
    public void testGetEmptyArray() {

        // tests that an empty array is returned , low boundary condition
        try {
            assertArrayEquals(new byte[0], testMainMemory.get(0, 0));
        } catch (InvalidAddressException e) {
            fail();
        }
    }

    @Test
    public void testGetNegativeArray() {

        // checks case when address is negative, we don't need to check
        // when both cases are true due to short-circuiting
        try {
            testMainMemory.get(-1, 1);
            fail();
        } catch (InvalidAddressException e) {
            // good
        }
    }

    @Test
    public void testGetOverLength() {

        // checks case when length is more than remaining memory size
        try {
            testMainMemory.get(0, 18);
            fail();
        } catch (InvalidAddressException e) {
            // good
        }
    }

    @Test
    public void testGetBoundaryHigh() {

        // checks case when length is barely remaining memory size
        try {
            testMainMemory.get(0, 16);
        } catch (InvalidAddressException e) {
            fail();
        }
    }

    @Test
    public void testGetAverageUseCase() {

        // checks an example average use case
        try {
            testMainMemory.get(4, 11);
        } catch (InvalidAddressException e) {
            fail();
        }
    }

    @Test
    public void testSetEmptyArray() {
        // tests that an empty array is returned, low boundary condition
        byte[] value = {};
        try {
            byte[] beforeSet = testMainMemory.get(0, 0);
            testMainMemory.set(0, value);
            assertArrayEquals(beforeSet, testMainMemory.get(0, 0));
        } catch (InvalidAddressException e) {
            fail();
        }
    }

    @Test
    public void testSetNegativeArray() {
        // checks case when address is negative, we don't need to check
        byte[] value = {1, 4, 5};
        try {
            testMainMemory.set(-1, value);
            fail();
        } catch (InvalidAddressException e) {
            // all good
        }
    }

    @Test
    public void testSetOverLength() {
        // checks case when length of value
        // is more than remaining memory size
        byte[] value = {1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        try {
            testMainMemory.set(3, value);
            fail();
        } catch (InvalidAddressException e) {
            // all good
        }
    }

    @Test
    public void testSetBoundaryHigh() {
        // checks case when length of value is barely remaining memory size
        byte[] value = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        try {
            testMainMemory.set(0, value);
            assertArrayEquals(value, (testMainMemory.get(0, 16)));
        } catch (InvalidAddressException e) {
            fail();
        }
    }

    @Test
    public void testSetAverageUseCase() {
        // checks an average example use case
        byte[] value = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] valueSubset = {2, 3, 4, 5, 6, 7, 8, 9};

        try {
            testMainMemory.set(4, value);
            assertArrayEquals(valueSubset, testMainMemory.get(6, 8));
        } catch (InvalidAddressException e) {
            fail();
        }
    }
}

