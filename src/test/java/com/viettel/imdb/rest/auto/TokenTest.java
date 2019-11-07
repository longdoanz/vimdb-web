package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.TestHelper;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TokenTest extends TestHelper {
    @Test
    public void test2Client() {
        String username = "admin";
        String password = "admin";
        String token = createAuthenticationToken(username, password, HttpStatus.OK, null);
        System.out.println("Token: " + token);
        String token2 = createAuthenticationToken(username, password, HttpStatus.OK, null);
        System.out.println("Token2: " + token2);
        Assert.assertNotEquals(token, token2, "TWO token must NOT EQUAL");
    }
}
