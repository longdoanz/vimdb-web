package com.viettel.imdb.rest.controller;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.rest.mock.server.ClusterSimulator;
import com.viettel.imdb.rest.model.CallbackResponse;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import io.trane.future.CheckedFutureException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UnitTest {

    @Autowired
    IMDBClient client;

    private static final IMDBEncodeDecoder encodeDecoder = IMDBEncodeDecoder.getInstance();

    @Test
    public void testCreateAndInsert() throws CheckedFutureException {
        String tableName = "TABLE_01";
        String key = "KEY_01";
        client.createTable(tableName).get(Duration.ofMinutes(1));

        List<Field> fields = new ArrayList<Field>() {{
            add(new Field("FIELD_1", encodeDecoder.encodeString("1234")));
            add(new Field("FIELD_2", encodeDecoder.encodeString("12345")));
        }};

        client.insert(tableName, key, fields).get(Duration.ofMinutes(1));

        when(client.select(tableName, key).get(Duration.ofMinutes(1))).thenReturn(new Record(fields));
    }
}
