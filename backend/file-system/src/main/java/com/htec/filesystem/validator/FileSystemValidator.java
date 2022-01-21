package com.htec.filesystem.validator;

import com.htec.filesystem.exception.ExceptionSupplier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystemValidator {

    private static final String REGEX_PATTERN = "^[A-Za-z _]*[A-Za-z0-9][A-Za-z _!@#$%^&-_+]{1,50}$";

    private static final Pattern pattern = Pattern.compile(REGEX_PATTERN);

    public void isShelfNameValid(String shelfName) {

        Matcher matcher = pattern.matcher(shelfName);
        if (!matcher.matches()) {
            throw ExceptionSupplier.shelfNameNotValid.get();
        }
    }

    public void isFileNameValid(String fileName) {

        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) {
            throw ExceptionSupplier.fileNameNotValid.get();
        }
    }

    public void isFolderNameValid(String folderName) {

        Matcher matcher = pattern.matcher(folderName);
        if (!matcher.matches()) {
            throw ExceptionSupplier.folderNameNotValid.get();
        }
    }
}
