package com.auth.wow.libre.infrastructure.conf;

import com.auth.wow.libre.infrastructure.util.*;
import org.junit.jupiter.api.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

class ConfigRandomSerialTest {
    private final ApplicationContext context = new AnnotationConfigApplicationContext(ConfigRandomSerial.class);

    @Test
    void testConfigRandomStringBeans() {
        Object[][] testCases = {
                {"recover-password", 5},
                {"random-string", 15},
                {"random-username", 5}
        };

        for (Object[] testCase : testCases) {
            String beanName = (String) testCase[0];
            int expectedLength = (int) testCase[1];

            RandomString randomString = (RandomString) context.getBean(beanName);
            assertNotNull(randomString, "Bean " + beanName + " should not be null");
            assertEquals(expectedLength, randomString.nextString().length(),
                    "Bean " + beanName + " should generate strings of length " + expectedLength);
        }
    }
}
