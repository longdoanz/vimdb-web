package com.viettel.imdb.rest.auto;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest extends TestHelper {

    @Test(priority = 2)
    public void testCreateDropUser() {
        String userName = "USER_01";

        String role1 = "read-write.data.CustInfo";
        String role2 = "read-write.data.MappingSubCust";

        dropUser(userName);

        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"roles\": [\n" +
                "    \"" + role1 + "\",\n" +
                "    \"" + role2 + "\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + userName + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.CREATED);
        List<String> expectedRoles = new ArrayList<String>() {{
            add(role1);
            add(role2);
        }};
        getUser(userName).andExpect(HttpStatus.OK).andExpectResponse("$.roles.[*].name", expectedRoles);
        dropUser(userName).andExpect(HttpStatus.NO_CONTENT);
        getUser(userName).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test(priority = 3)
    public void testCreate_Get_Update_Drop() {
        String userName = "USER_03";
        dropUser(userName);
        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"roles\": [\n" +
                "    \"read-write.data.CustInfo\",\n" +
                "    \"read-write.data.MappingSubCust\",\n" +
                "    \"read-write.user.SessionData\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + userName + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.CREATED);
        getUser(userName).andExpect(HttpStatus.OK);

        String updateBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"roles\": [" +
                "]\n" +
                "}";
        updateUser(userName, updateBody);
        getUser(userName).andExpect(HttpStatus.OK);
        dropUser(userName);
        getUser(userName).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test(priority = 4)
    public void test_Create_Existed_Role_In_User() {
        String userName = "USER_03";
        dropUser(userName);

        //language=JSON
        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \"read-write.data.MappingSubCust\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"table01\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"roles\": [\n" +
                "    \"read-write.data.CustInfo\",\n" +
                "    \"read-write.data.MappingSubCust\",\n" +
                "    \"read-write.user.SessionData\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + userName + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.BAD_REQUEST);
        getUser(userName).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test(priority = 5)
    public void test_Read_User_Do_Not_Existed() {
        String username = "USER_DO_NOT_EXISTED";
        getUser(username).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test
    public void test01() {
        String data = "{\"username\":\"USER_01\",\"authenticationMethod\":\"RBAC\",\"roles\":[{\"name\":\"read-write.data.CustInfo\",\"privileges\":[{\"permission\":\"read\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"CustInfo\"}},{\"permission\":\"write\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"CustInfo\"}}]},{\"name\":\"read-write.data.MappingSubCust\",\"privileges\":[{\"permission\":\"read\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"MappingSubCust\"}},{\"permission\":\"write\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"MappingSubCust\"}}]}]}";
        List responseRoles = JsonPath.parse(data).read("roles", List.class);
        System.out.println(responseRoles);

        System.out.println(JsonPath.parse(data).read("$.roles.[*].name", List.class));
    }
}
