/*
 * Copyright 2019-Current jittagornp.me
 */
package me.jittagornp.example.reactive.util;

import java.util.Arrays;

/**
 * @author jitta
 */
public class ByteUtils {

    private ByteUtils() {
        
    }

    public static byte[] xor(final byte[] input1, final byte[] input2) {
        byte[] data1 = Arrays.copyOf(input1, input1.length);
        byte[] data2 = Arrays.copyOf(input2, input2.length);
        // make data2 the largest...
        if (data1.length > data2.length) {
            final byte[] tmp = data2;
            data2 = data1;
            data1 = tmp;
        }

        for (int i = 0; i < data1.length; i++) {
            data2[i] ^= data1[i];
        }

        return data2;
    }

}
