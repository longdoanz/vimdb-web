package com.viettel.imdb.rest.service;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.JavaClient;
import com.viettel.imdb.common.ClientConfig;
import com.viettel.imdb.rest.common.Common;
import com.viettel.imdb.rest.common.HTTPRequest;
import com.viettel.imdb.rest.common.TestUtil;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;

import static com.viettel.imdb.rest.common.Common.HOST_URL;
import static org.testng.Assert.*;

/**
 * @author quannh22
 * @since 04/10/2019
 */
public class AuthServiceImplTest extends TestUtil {


    private static final String USER_TABLENAME = "user";
    private static final int USER_COUNT = 1000;
    private static final String USERNAME_PREFIX = "USER_";
    private static final String USER_PASSWORD = "PASSWORD";

    private static final String ROLE_TABLENAME = "role";
    private static final int ROLE_COUNT = 1000;
    private static final String ROLENAME_PREFIX = "ROLE_";


    @BeforeClass
    public void beforeTest_CreateSomeUsers() throws InterruptedException {
        final Duration TIMEOUT = Duration.ofMillis(Integer.MAX_VALUE);
        IMDBClient adminClient = new JavaClient("172.16.31.54:12345", new ClientConfig(true, "admin", "admin"));

        for(int i = 0; i < ROLE_COUNT; i++) {
            try {
                adminClient.deleteUser(USERNAME_PREFIX + i).get(TIMEOUT);
            } catch (Exception ignored) {
            }

            try {
                adminClient.createUser(USERNAME_PREFIX+ i, USER_PASSWORD.getBytes()).get(TIMEOUT);
            } catch (Exception ex) {
                ex.printStackTrace();
                Assert.fail("create user failed!", ex);
            }
        }


        for(int i = 0; i < ROLE_COUNT; i++) {
            try {
                adminClient.deleteRole(ROLENAME_PREFIX + i).get(TIMEOUT);
            } catch (Exception ignored) {
            }

            try {
                adminClient.createRole(ROLENAME_PREFIX + i).get(TIMEOUT);
            } catch (Exception ex) {
                ex.printStackTrace();
                Assert.fail("create role failed!", ex);
            }
        }





        // user[i] has role[i]
        for(int i = 0; i < 7 * USER_COUNT/10; i++) {
            try {
                int finalI = i;
                adminClient.updateUser(USERNAME_PREFIX + i, new ArrayList<String>(){{
                    add(ROLENAME_PREFIX + finalI);
                }}).get(TIMEOUT);
            } catch (Exception ex) {
                ex.printStackTrace();
                Assert.fail("update user failed!", ex);
            }
        }

        for(int i = 7 * USER_COUNT/10; i < 8 * USER_COUNT/10; i++) {
            try {
                int finalI = i;
                adminClient.updateUser(USERNAME_PREFIX + i, new ArrayList<String>(){{
                    add(ROLENAME_PREFIX + (9 * ROLE_COUNT / 10));
                }}).get(TIMEOUT);
            } catch (Exception ex) {
                ex.printStackTrace();
                Assert.fail("update user failed!", ex);
            }
        }

        for(int i = 8 * USER_COUNT/10; i < 10 * USER_COUNT/10; i++) {
            try {
                int finalI = i;
                adminClient.updateUser(USERNAME_PREFIX + i, new ArrayList<String>(){{
                    add(ROLENAME_PREFIX + finalI);
                }}).get(TIMEOUT);
            } catch (Exception ex) {
                ex.printStackTrace();
                Assert.fail("update user failed!", ex);
            }
        }

        adminClient.close();
    }
    @Test(priority = 2)
    public void testConnectAdminUser() {
        String username = "admin";
        String password = "admin";
        String token = testLogin(username, password, HttpStatus.OK, null);
        System.out.println("Token: " + token);
    }

    /*@Test(priority = 3)
    public void testConnectWrongUser() {
        String username = "admin1";
        String password = "admin1";
        String token = testLogin(username, password, HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create a new client. Contact admin for more detail");
        System.out.println("Token: " + token);
    }*/

    @Test(priority = 4)
    public void testConnectManyUserExceedThreshold() {
        for(int i = 0; i < 50; i++) {
            System.out.println("Test: " + i);
            String username = USERNAME_PREFIX + i;
            String password = USER_PASSWORD;
            String token = testLogin(username, password, HttpStatus.OK, null);
            System.out.println("Token: " + token);
        }
    }


}