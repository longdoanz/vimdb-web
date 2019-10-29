package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.TestUtil;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

public class TestHelper extends TestUtil {
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // TODO REMOVE ALL TABLE
        }
    }
}
