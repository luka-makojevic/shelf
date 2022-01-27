package com.htec.account.util;

import java.nio.file.FileSystems;

public class PathConstants {
    public final static String HOME_PATH = System.getProperty("user.home");
    public final static String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();
    public final static String USER_PATH = PATH_SEPARATOR + "shelf-files" + PATH_SEPARATOR + "user-data" + PATH_SEPARATOR;
}
