package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.HttpResponse;
import com.viettel.imdb.rest.common.TestHelper;
import org.pmw.tinylog.Logger;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class UserTest extends TestHelper {

    public void createRoleUtil(){
        String rolename1 = "role1";
        String rolename2 = "role2";
        dropRole(rolename1);
        dropRole(rolename2);

        String bodyrole1 = "{\n" +
                "  \"name\": \""+rolename1+"\",\n" +
                "  \"privileges\": [\n" +
                "    {\n" +
                "      \"permission\": \"read\",\n" +
                "      \"resource\": {\n" +
                "        \"name\": \"data\",\n" +
                "        \"namespace\": \"NAMESPACE\",\n" +
                "        \"table\": \"CustInfo\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        String bodyrole2 = "{\n" +
                "  \"name\": \""+rolename2+"\",\n" +
                "  \"privileges\": [\n" +
                "    {\n" +
                "      \"permission\": \"*\",\n" +
                "      \"resource\": {\n" +
                "        \"name\": \"user\",\n" +
//                "        \"namespace\": \"NAMESPACE\",\n" +
                "        \"user\": \"thanh\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        createRole(bodyrole1).andExpect(HttpStatus.CREATED);

        HttpResponse res = getRole(rolename1);
        res.prettyPrint();
        res.andExpect(HttpStatus.OK);
        res.andExpectResponse(bodyrole1);

        createRole(bodyrole2).andExpect(HttpStatus.CREATED);

        HttpResponse res2 = getRole(rolename2);
        res2.prettyPrint();
        res2.andExpect(HttpStatus.OK);
        res2.andExpectResponse(bodyrole2);


    }

    public HttpResponse createUser(String username, String newRolename){

//        dropUser(username);
//        dropRole(newRolename);

        //createRole();

        String body = "{\n" +
                "  \"userName\": \"" + username + "\",\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"roles\": [\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRolename+"\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"tablec89977\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        //getUser(username).prettyPrint();
        return createUser(body);

//        getUser(username).prettyPrint().andExpect(HttpStatus.OK);
//
//        dropUser(username).andExpect(HttpStatus.NO_CONTENT);

    }


    @Test(priority = 2)
    public void test_Get_All_User() {
        getUser().andExpect(HttpStatus.OK).prettyPrint();
    }

    @Test(priority = 3)
    public void test_Create_Drop_User() {
        String username = "USER_01";

        String role1 = "admin";
        String role2 = "ROLE_02";
        dropRole(role2);
        String body = "{\n" +
                "  \"name\": \""+ role2 +"\"\n" +
                "}";

        createRole(body).andExpect(HttpStatus.CREATED);

        dropUser(username);

        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"roles\": [\n" +
                "    \"" + role1 + "\",\n" +
                "    \"" + role2 + "\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.CREATED);
        List<String> expectedRoles = new ArrayList<String>() {{
            add(role2);
            add(role1);
        }};
        getUser(username).andExpect(HttpStatus.OK);//.andExpectResponse("$.roles.[*].name", expectedRoles);
        dropUser(username).andExpect(HttpStatus.NO_CONTENT);
        getUser(username).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test(priority = 4)
    public void testCreate_Get_Update_Drop() {
        String newRole1 = "roletest1";
        String newRole2 = "roletest2";
        dropRole(newRole1);
        dropRole(newRole2);
        String username = "USER_03";
        dropUser(username);
        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole1+"\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"tablec89977\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"roles\": [\n" +
                "    \"admin\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.CREATED);
        getUser(username).andExpect(HttpStatus.OK);

        String updateBody = "{\n" +
                "\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole2+"\",\n" +
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
                "    \"admin\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        updateUser(username, updateBody).andExpect(HttpStatus.NO_CONTENT);
        Logger.info(getUser(username).andExpect(HttpStatus.OK).getResponse());
        dropUser(username);
        getUser(username).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test(priority = 4)
    public void test_Create_Existed_Role_In_User() {

        String username = "USER_03";
        dropUser(username);


        //language=JSON
        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \"role1\",\n" +
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
                "    \"admin\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.BAD_REQUEST);
        getUser(username).andExpect(HttpStatus.NOT_FOUND);
    }
    @Test(priority = 4)
    public void _2_3_3_createUser_success() throws Exception {
        String rolename1 = "role1";
        String rolename2 = "role2";
        String username = "adfjh_a-d9f";
        String newRolename = "roletest1";


        dropUser(username);
        dropRole(newRolename);

        createRoleUtil();

        String body = "{\n" +
                "  \"userName\": \"" + username + "\",\n" +
                "  \"password\": \"admin13\",\n" +
                "  \"roles\": [\n" +
                "    \"role1\"\n" +
                "  ],\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRolename+"\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"tablec89977\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String body2 = "{\n" +
                "  \"userName\": \"" + username + "\",\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"roles\": [\n" +
                "    \"role1\"\n" +
                "  ],\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRolename+"\",\n" +
                "      \"privileges\": [\n" +
                "       {\n" +
                "         \"permission\": \"read\",\n" +
                "         \"resource\": {\n" +
                "           \"name\": \"data\",\n" +
                "           \"namespace\": \"NAMESPACE\",\n" +
                "           \"table\": \"table01\"\n" +
                "         }\n" +
                "       }  \n" +
                "     ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        Logger.info(body.equals(body2));
        getUser(username).prettyPrint();
        createUser(body2).andExpect(HttpStatus.CREATED);
        Thread.sleep(2000);

        getUser(username).prettyPrint().andExpect(HttpStatus.OK);

        dropUser(username).andExpect(HttpStatus.NO_CONTENT);

    }

    @Test(priority = 5)
    public void test_Read_User_Do_Not_Existed() {
        String username = "USER_DO_NOT_EXISTED";
        getUser(username).andExpect(HttpStatus.NOT_FOUND);
    }

    @Test(priority = 5)
    public void create_Use_Existed() {
        String username = "thurv";
        String username2 = "thurv2";
        String newRole1 = "newRole1";
        String newRole2 = "newRole2";
        dropUser(username);
        dropRole(newRole1);
        dropRole(newRole2);


        createUser(username, newRole1);
        createUser(username, newRole2).andExpect(HttpStatus.BAD_REQUEST).prettyPrint();
//        getRole(newRole1).andExpect(HttpStatus.OK);
//        getRole(newRole2).andExpect(HttpStatus.NOT_FOUND);
    }
    @Test(priority = 5)
    public void change_password_not_sucess() throws Exception {
        String username = "thurv";
        String newRole1 = "newRole45";
        String newRole2 = "newRole4";
        dropUser(username);
        dropRole(newRole1);
        dropRole(newRole2);

        createRoleUtil();

        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole1+"\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"tablec89977\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"roles\": [\n" +
                "    \"admin\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.CREATED);
        getUser(username);
//        http.authorize(username, "admin13");

        String updateBody = "{\n" +
                "\n" +
                "  \"password\": \"6969\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole1+"\",\n" +
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
                "    \"admin\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";

        //getRole(newRole1).andExpect(HttpStatus.OK);
        updateUser(username, updateBody).prettyPrint().andExpect(HttpStatus.BAD_REQUEST);
        //http.authorize(username, "admin13");

    }
    @Test(priority = 7)
    public void test_not_password(){
        String username = "thurv";
        String password = "";
        String newRole1 = "newRole45";
        dropUser(username);
        dropRole(newRole1);

        createRoleUtil();

        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \""+password+"\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole1+"\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"tablec89977\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"roles\": [\n" +
                "    \"admin\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.BAD_REQUEST);
    }
    @Test(priority = 7)
    public void change_password() throws Exception {
        String username = "thurv";
        String password = "Admin@123";
        String newRole1 = "newRole45";
        dropUser(username);
        dropRole(newRole1);

        createUser(username, newRole1).andExpect(HttpStatus.CREATED);

        String updateUserBody = "{\n" +
                "\n" +
                "  \"password\": \""+password+"\"\n" +
                "}";
        http.authorize("thurv", password);
//        http.authorize("ldt", "123");
        updateUser(username, updateUserBody).andExpect(HttpStatus.NO_CONTENT);
        getUser(username).prettyPrint();
        http.authorize(username, password);
    }
    @Test(priority = 8)
    public void create_user_with_role_not_existed(){
        String username = "thurv";
        String password = "Admin@123";
        String newRole1 = "newRole";
        dropUser(username);
        dropRole(newRole1);
        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \""+password+"\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole1+"\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"tablec89977\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"roles\": [\n" +
                "    \"aqdbjf\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.NOT_FOUND).prettyPrint();
        getUser(username).andExpect(HttpStatus.NOT_FOUND);

        dropUser(username);
        dropRole(newRole1);

    }

    @Test(priority = 9)
    public void get_user_have_no_role(){
        String username = "thurv";
        String password = "Admin@123";
        dropUser(username);
        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \""+password+"\",\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.CREATED);
        getUser(username).andExpect(HttpStatus.OK);

    }
    @Test(priority = 10)
    public void update_user_not_sucess() throws Exception {
        String username = "thurv";
        String newRole1 = "newRole45";
        String newRole2 = "newRole4";
        dropUser(username);
        dropRole(newRole1);
        dropRole(newRole2);

        createRoleUtil();

        String createUserBody = "{\n" +
                "\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole1+"\",\n" +
                "      \"privileges\": [\n" +
                "        {\n" +
                "          \"permission\": \"read\",\n" +
                "          \"resource\": {\n" +
                "            \"name\": \"data\",\n" +
                "            \"namespace\": \"NAMESPACE\",\n" +
                "            \"table\": \"tablec89977\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"roles\": [\n" +
                "    \"admin\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";
        createUser(createUserBody).andExpect(HttpStatus.CREATED);
        getUser(username);
//        http.authorize(username, "admin13");

        String updateBody = "{\n" +
                "\n" +
                "  \"password\": \"Admin@123\",\n" +
                "  \"newRoles\": [\n" +
                "    {\n" +
                "      \"name\": \""+newRole2+"\",\n" +
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
                "    \"assss\",\n" +
                "    \"admin\",\n" +
                "    \"admin\"\n" +
                "  ],\n" +
                "  \"userName\": \"" + username + "\"\n" +
                "}";

        getRole(newRole1).andExpect(HttpStatus.OK);
        getRole(newRole2).andExpect(HttpStatus.NOT_FOUND);
        updateUser(username, updateBody).prettyPrint().andExpect(HttpStatus.NOT_FOUND);
        //http.authorize(username, "admin13");

    }
    /*@Test
    public void test01() {
        String data = "{\"userName\":\"USER_01\",\"authenticationMethod\":\"RBAC\",\"roles\":[{\"name\":\"read-write.data.CustInfo\",\"privileges\":[{\"permission\":\"read\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"CustInfo\"}},{\"permission\":\"write\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"CustInfo\"}}]},{\"name\":\"read-write.data.MappingSubCust\",\"privileges\":[{\"permission\":\"read\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"MappingSubCust\"}},{\"permission\":\"write\",\"resource\":{\"name\":\"data\",\"namespace\":\"NAMESPACE\",\"table\":\"MappingSubCust\"}}]}]}";
        List responseRoles = JsonPath.parse(data).read("roles", List.class);
        System.out.println(responseRoles);

        System.out.println(JsonPath.parse(data).read("$.roles.[*].name", List.class));
    }*/
}
