/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/11/18
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
    public void shouldBe00000000() {
        byte[] input1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        byte[] input2 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe11111111() {
        byte[] input1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe00000000_whenOneAll() {
        byte[] input1 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe00001111() {
        byte[] input1 = new byte[]{1, 1, 1, 1};
        byte[] input2 = new byte[]{1, 1, 1, 1, 1, 1, 1, 1};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{0, 0, 0, 0, 1, 1, 1, 1};
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe11110000() {
        byte[] input1 = new byte[]{1, 1, 1, 1, 0, 0, 0, 0};
        byte[] input2 = new byte[]{0, 0, 0, 0};
        byte[] output = ByteUtils.xor(input1, input2);
        byte[] expected = new byte[]{1, 1, 1, 1, 0, 0, 0, 0};
        assertThat(output).isEqualTo(expected);
    }
}
