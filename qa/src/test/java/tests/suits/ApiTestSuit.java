package tests.suits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.backendTests.ApiTest;

    @RunWith(Suite.class)

    @Suite.SuiteClasses({
            ApiTest.class
    })
    public class ApiTestSuit {
    }
