/*
package com.viettel.imdb.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.imdb.rest.model.TableModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static com.viettel.imdb.rest.common.Common.DATA_PATH;
import static com.viettel.imdb.rest.common.Common.LOGIN_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DataControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;


    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        if(token == null) {
            Logger.error("--------- Start LOGIN ------------");
            getToken();
        }
    }


    private void getToken() throws Exception {

        JsonNode body = objectMapper.readTree("{\n" +
                "  \"username\": \"admin\",\n" +
                "  \"password\": \"admin\"\n" +
                "}");

        MvcResult mvcResult = mockMvc.perform(post(LOGIN_PATH)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
            .andReturn();

        mvcResult.getAsyncResult();

        MockHttpServletResponse res = mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        System.err.println("-------------------------------------------------- GET TOKEN --------------------------");
        System.err.println(res.getContentAsString());

        token = objectMapper.readTree(res.getContentAsString()).get("token").toString();
    }

    @Test
    public void testCreateNamespace() throws Exception {
        TableModel tableModel = new TableModel("TABLE01");
        System.out.println(objectMapper.writeValueAsString(tableModel));

        mockMvc.perform(post(DATA_PATH)
                .contentType("application/json")
//                .param("test", "01")
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(tableModel))
        ).andExpect(status().isInternalServerError());
    }

    @Test
    public void testCreateTable() throws Exception {
        TableModel tableModel = new TableModel("TABLE01");
        System.out.println(objectMapper.writeValueAsString(tableModel));

        mockMvc.perform(post(DATA_PATH + "/namespace")
                .contentType("application/json")
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(tableModel))
        ).andExpect(status().isOk())
        .andDo(print())
        ;
    }

    */
/*@Test
    public void givenCountMethodMocked_WhenCountInvoked_ThenMockValueReturned() {
        DataService service = new DataServiceImpl();
        Mockito.when(count()).thenReturn(123);


//        Assert.assertEquals(123L, userCount);
        Mockito.verify(count());
    }

    int count() {
        return 234;
    }*//*

}
*/
