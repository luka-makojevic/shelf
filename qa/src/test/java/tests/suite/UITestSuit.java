package tests.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.frontendTests.LoginTest;

@RunWith(Suite.class)
    @Suite.SuiteClasses({
            LoginTest.class
    })
    public class UITestSuit {
    }
