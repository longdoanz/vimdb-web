package com.viettel.imdb.rest.service;

import com.viettel.imdb.core.security.User;
import com.viettel.imdb.rest.common.TestUtil;
import com.viettel.imdb.rest.model.AddUserRequest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quannh22
 * @since 15/10/2019
 */
public class SecurityServiceImplTest extends TestUtil {
//    private HTTPRequest http;
//    private IMDBEncodeDecoder encoder;
//    @BeforeMethod
//    public void setUp() throws Exception {
//        http = new HTTPRequest(HOST_URL);
//        encoder = IMDBEncodeDecoder.getInstance();
//    }

    @Test
    public void testGetUsers() {
        // 1. first - only one user found - admin
        List<User> userList = new ArrayList<>();
        userList.add(new User("admin", "RBAC", new ArrayList<String>() {{
            add("admin");
        }}));
        testGetUsers(HttpStatus.OK, userList);
        // 2. create user
        AddUserRequest addUserRequest = new AddUserRequest("quannh22", "123456a@", new ArrayList<String>() {{
            add("admin");
        }}, new ArrayList<>());
        testAddUser(addUserRequest, HttpStatus.CREATED, null);

        userList.add(new User("quannh22", "123456a@", new ArrayList<String>() {{
            add("admin");
        }}));

        testGetUsers(HttpStatus.OK, userList);
    }

    @Test
    public void testGetUser() {
    }

    @Test
    public void testAddUser() {
    }

    @Test
    public void testEditUser() {
    }

    @Test
    public void testDeleteUser() {
    }

    @Test
    public void testGetRoles() {
    }

    @Test
    public void testGetRole() {
        testRole();
    }

    @Test
    public void testAddRole() {
    }

    @Test
    public void testEditRole() {
    }

    @Test
    public void testDeleteRole() {
    }

    @Test
    public void testGetAuditLogs() {
        tesAuditLog();
    }
}