package it.shorty.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class HashUtilsTest {

    @Test
    void generateRandomCode_shouldReturnCodeWithCorrectLength() {
        String code = HashUtils.generateRandomCode();
        Assertions.assertNotNull(code);
        Assertions.assertEquals(8, code.length());
    }

    @Test
    void generateRandomCode_shouldGenerateDifferentCodes() {
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            codes.add(HashUtils.generateRandomCode());
        }
        Assertions.assertEquals(100, codes.size());
    }
}
