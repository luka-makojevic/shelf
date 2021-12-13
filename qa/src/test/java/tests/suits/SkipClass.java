package tests.suits;

import org.junit.Assume;

public class SkipClass{

    public static void IF(Boolean condition){
        Assume.assumeFalse(condition);
    }
    public static void UNLESS(Boolean condition){
        Assume.assumeTrue(condition);
    }
}