package io.devopsnextgenx.microservices.modules.exception;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppExceptionTest {
    @Test
    public void testDuplicates() {
        Map<String, String> map = new HashMap<>();
        List<AppException.ERROR_CODE> errorList = Arrays.stream(AppException.ERROR_CODE.values()).collect(Collectors.toList());
        errorList.forEach(error_code -> {
            String errorName = error_code.name();
            String errorNumber = error_code.toString();
            String oldValue = map.put(errorNumber, errorName);
            if (oldValue != null) {
                Assert.fail(String.format("Error %s cannot be added, %s Exists Twice, once for %s and once for %s ", errorName, error_code, errorName, oldValue));
            }
        });

    }
}