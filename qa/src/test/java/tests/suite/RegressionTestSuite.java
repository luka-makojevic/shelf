package tests.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.backendTests.ApiUserTest;
import tests.frontendTests.LoginTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        LoginTest.class,
        ApiUserTest.class
})
public class RegressionTestSuite {
}
