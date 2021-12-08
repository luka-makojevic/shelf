package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        LoginTest.class,
        ApiTest.class
})
public class JUnitTestSuite {
}
