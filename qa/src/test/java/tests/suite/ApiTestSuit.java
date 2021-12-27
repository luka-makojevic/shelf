package tests.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.backendTests.ApiUserTest;

    @RunWith(Suite.class)

    @Suite.SuiteClasses({
            ApiUserTest.class
    })
    public class ApiTestSuit {
    }
