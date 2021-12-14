package tests.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.backendTests.ApiTest;
import tests.frontendTests.LoginTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        LoginTest.class,
        ApiTest.class
})
public class RegressionTestSuite {
}
