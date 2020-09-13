/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author jitta
 */
public class ByteUtils_xorTest {
    
    /*
     * A | B | answer 
     * --------------
     * 0 | 0 | 0 
     * 0 | 1 | 1 
     * 1 | 0 | 1 
     * 1 | 1 | 0
     */
    @Test
    @DisplayName("Should be 00000000 when all 0")
    public void shouldBe00000000_whenAll0() {
        final byte[] input1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        final byte[] input2 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        final byte[] output = ByteUtils.xor(input1, input2);
        final byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Should be 11111111")
    public void shouldBe11111111() {
        final byte[] input1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        final byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        final byte[] output = ByteUtils.xor(input1, input2);
        final byte[] expected = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Should be 00000000 when all 1")
    public void shouldBe00000000_whenAll1() {
        final byte[] input1 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        final byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        final byte[] output = ByteUtils.xor(input1, input2);
        final byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Should be 00001111")
    public void shouldBe00001111() {
        final byte[] input1 = new byte[]{1, 1, 1, 1};
        final byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        final byte[] output = ByteUtils.xor(input1, input2);
        final byte[] expected = new byte[]{0, 0, 0, 0, 1, 1, 1, 1};
        assertArrayEquals(expected, output);
    }

    @Test
    @DisplayName("Should be 11110000")
    public void shouldBe11110000() {
        final byte[] input1 = new byte[]{1, 1, 1, 1, 0, 0, 0, 0};
        final byte[] input2 = new byte[]{0, 0, 0, 0};
        final byte[] output = ByteUtils.xor(input1, input2);
        final byte[] expected = new byte[]{1, 1, 1, 1, 0, 0, 0, 0};
        assertArrayEquals(expected, output);
    }

    @Test
    @Disabled
    @DisplayName("Disabled/Skip this test")
    public void disabledThisTest(){

    }
}
