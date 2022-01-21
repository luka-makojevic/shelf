package com.htec.validator;

import com.htec.shelffunction.exception.ExceptionSupplier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionValidator {

    private static final String SHELF_NAME_PATTERN = "^[A-Za-z _]*[A-Za-z0-9][A-Za-z _!@#$%^&-_+]{1,50}$";

    private static final Pattern pattern = Pattern.compile(SHELF_NAME_PATTERN);

    public void isFunctionNameValid(String functionName) {

        Matcher matcher = pattern.matcher(functionName);
        if (!matcher.matches()) {
            throw ExceptionSupplier.functionNameNotValid.get();
        }
    }
}
