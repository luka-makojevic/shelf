package com.htec.shelffunction.util;

import static com.htec.shelffunction.util.PathConstants.HOME_PATH;
import static com.htec.shelffunction.util.PathConstants.USER_PATH;

public class LanguageConstants {
    public final static String JAVA = "java";
    public final static String CS = "csharp";

    public final static String JAVA_EXECUTE_CMD = "java -cp ";
    public final static String CS_EXECUTE_CMD = "mcs ";

    public final static String JAVA_COMPILE_CMD = "javac -cp ";
    public final static String CSHARP_COMPILE_CMD = "mcs ";

    public final static  String JAVA_EXTENSION = ".java";
    public final static  String CSHARP_EXTENSION = ".cs";

    public final static String JARS_PATH = HOME_PATH + USER_PATH + "predefined_functions/jars/*";

    public final static Long TIME_OUT = 50L;
}
