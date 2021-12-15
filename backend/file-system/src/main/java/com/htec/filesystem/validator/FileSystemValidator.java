package com.htec.filesystem.validator;

import com.htec.filesystem.exception.ExceptionSupplier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystemValidator {

    private static final String SHELF_NAME_PATTERN = "^.{1,50}$";

    private static final Pattern pattern = Pattern.compile(SHELF_NAME_PATTERN);

    public void isShelfNameValid(String shelfName) {

        Matcher matcher = pattern.matcher(shelfName);
        if (!matcher.matches()) {
            throw ExceptionSupplier.shelfNameNotValid.get();
        }
    }
}
