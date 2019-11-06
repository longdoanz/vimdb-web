package com.viettel.imdb.rest.auto;

import com.viettel.imdb.rest.common.HttpResponse;
import com.viettel.imdb.rest.common.TestHelper;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.Collections;

public class RoleTest extends TestHelper {

    @Test(priority = 2)
    public void getAllRole() {
        HttpResponse res = getRole();
        res.prettyPrint();
        res.andExpect(HttpStatus.OK);
    }

    @Test(priority = 2)
    public void testCreateDropRole() {
        String roleName = "ROLE_01";
        String body = "{\n" +
                "  \"privileges\": [\n" +
                "{\n" +
                "        \"permission\": \"read\",\n" +
                "        \"resource\": {\n" +
                "          \"name\": \"data\",\n" +
                "          \"namespace\": \"NAMESPACE\",\n" +
                "          \"table\": \"SessionData\"\n" +
                "        }\n" +
                "      }" +
                "],\n" +
                "  \"name\": \"" + roleName + "\"\n" +
                "}";

        HttpResponse res = createRole(body);
        res.prettyPrint();
        res.andExpect(201);
        dropRole(roleName);
    }

    @Test(priority = 2)
    public void testDropAndGetRole() {
        String roleName = "ROLE_02";
        dropRole(roleName);
        String body = "{\n" +
                "  \"name\": \""+ roleName +"\"\n" +
                "}";

        createRole(body).andExpect(HttpStatus.CREATED);

        getRole(roleName).andExpect(HttpStatus.OK).andExpectResponse("privileges", Collections.emptyList());

        dropRole(roleName).andExpect(HttpStatus.NO_CONTENT);
    }


    @Test(priority = 3)
    public void testUpdateRole() {
        String roleName = "ROLE_03";
        dropRole(roleName);
        String body = "{\n" +
                "  \"name\": \""+ roleName +"\"\n" +
                "}";

        createRole(body).andExpect(HttpStatus.CREATED);

        getRole(roleName).andExpect(HttpStatus.OK).andExpectResponse("privileges", Collections.emptyList());

        dropRole(roleName).andExpect(HttpStatus.NO_CONTENT);
    }


    @Test(priority = 4)
    public void testPatchRole() {
        String roleName = "ROLE_04";
        String body = "{\n" +
                "  \"privileges\": [\n" +
                "{\n" +
                "        \"permission\": \"read\",\n" +
                "        \"resource\": {\n" +
                "          \"name\": \"data\",\n" +
                "          \"namespace\": \"NAMESPACE\",\n" +
                "          \"table\": \"SessionData\"\n" +
                "        }\n" +
                "      }" +
                "],\n" +
                "  \"name\": \"" + roleName + "\"\n" +
                "}";

        createRole(body);

        String updateRoleBody = "{\n" +
                "  \"name\": \""+ roleName +"\"\n" +
                "}";

        updateRole(roleName, updateRoleBody).andExpect(HttpStatus.NO_CONTENT);

        getRole(roleName).andExpect(HttpStatus.OK).andExpectResponse("privileges", Collections.emptyList());

        dropRole(roleName).andExpect(HttpStatus.NO_CONTENT);
    }

    @Test(priority = 4)
    public void test_Read_Role_Do_Not_Exist() {
        String roleName = "SOMETHING_DO_NOT_EXISTED";
        getRole(roleName).andExpect(HttpStatus.NOT_FOUND);
    }
}
