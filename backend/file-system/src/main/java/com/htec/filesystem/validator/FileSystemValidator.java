package com.htec.filesystem.validator;

import com.htec.filesystem.exception.ExceptionSupplier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystemValidator {

    private static final String REGEX_PATTERN = "^[A-Za-z0-9_!@#$%^&\\-_+][A-Za-z0-9 _!@#$%^&\\-_+]*$";

    private static final Pattern pattern = Pattern.compile(REGEX_PATTERN);

    public void isShelfNameValid(String shelfName) {

        Matcher matcher = pattern.matcher(shelfName);
        if (!matcher.matches() || shelfName.length() > 50) {
            throw ExceptionSupplier.shelfNameNotValid.get();
        }
    }

    public void isFileNameValid(String fileName) {

        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches() || fileName.length() > 50) {
            throw ExceptionSupplier.fileNameNotValid.get();
        }
    }

    public void isFolderNameValid(String folderName) {

        Matcher matcher = pattern.matcher(folderName);
        if (!matcher.matches() || folderName.length() > 50) {
            throw ExceptionSupplier.folderNameNotValid.get();
        }
    }
}
