package com.viettel.imdb.rest.common;

import com.viettel.imdb.rest.domain.RestIndexModel;
import com.viettel.imdb.util.CBOREncodeDecoderNew;
import com.viettel.imdb.util.IMDBEncodeDecoder;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.Map;

import static com.viettel.imdb.rest.common.Common.*;

/**
 * @author quannh22
 * @since 03/09/2019
 */
public class TestUtil {
    private HTTPRequest http;
    private IMDBEncodeDecoder encoder;
    @BeforeMethod
    public void setUp() throws Exception {
        http = new HTTPRequest(HOST_URL);
        encoder = new CBOREncodeDecoderNew();
    }

    public void testCreateTable(String tableName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            Map res = http.sendPost(DATA_PATH_WITH_NAMESPACE, tableName);
            System.out.println(res);
            Assert.assertEquals(res.get("code"), expectedReturnCode.value());
            if(expectedResponse == null)
                Assert.assertNull(res.get("response"));
            else
                Assert.assertEquals(encoder.encodeJsonString(res.get("response").toString()), encoder.encodeJsonString(expectedResponse));
        }catch (Exception ex) {
            Assert.fail("Test create table failed!!!", ex);
        }
    }

    public void testDropTable(String tableName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            Map res = http.sendDelete(buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName));
            System.out.println(res);
            Assert.assertEquals(res.get("code"), expectedReturnCode.value());
            if(expectedResponse == null)
                Assert.assertNull(res.get("response"));
            else
                Assert.assertEquals(encoder.encodeJsonString(res.get("response").toString()), encoder.encodeJsonString(expectedResponse));
        }catch (Exception ex) {
            Assert.fail("Test drop table failed!!!", ex);
        }
    }

    public void testCreateIndex(String tableName, String indexName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            final String INDEX_TYPE = "numeric";
            RestIndexModel restIndexModel = new RestIndexModel(DEFAULT_NAMESPACE, tableName, INDEX_TYPE, indexName);
            String path = buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName);
            Map res = http.sendPost(path, restIndexModel.toJsonString());
            System.out.println(res);
            Assert.assertEquals(res.get("code"), expectedReturnCode.value());
            if(expectedResponse == null)
                Assert.assertNull(res.get("response"));
            else
                Assert.assertEquals(encoder.encodeJsonString(res.get("response").toString()), encoder.encodeJsonString(expectedResponse));
        } catch (Exception ex) {
            Assert.fail("Test create index failed!!!", ex);
        }
    }

    public void testCreateIndexString(String tableName, String indexName, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            final String INDEX_TYPE = "string";
            RestIndexModel restIndexModel = new RestIndexModel(DEFAULT_NAMESPACE, tableName, INDEX_TYPE, indexName);
            String path = buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName);
            Map res = http.sendPost(path, restIndexModel.toJsonString());
            System.out.println(res);
            Assert.assertEquals(res.get("code"), expectedReturnCode.value());
            if(expectedResponse == null)
                Assert.assertNull(res.get("response"));
            else
                Assert.assertEquals(encoder.encodeJsonString(res.get("response").toString()), encoder.encodeJsonString(expectedResponse));
        } catch (Exception ex) {
            Assert.fail("Test create index failed!!!", ex);
        }
    }


    public void testCreateIndexWithType(String tableName, String indexName, String type, HttpStatus expectedReturnCode, String expectedResponse) {
        try {
            RestIndexModel restIndexModel = new RestIndexModel(DEFAULT_NAMESPACE, tableName, type, indexName);
            String path = buildFromPath(DATA_PATH_WITH_NAMESPACE, tableName);
            Map res = http.sendPost(path, restIndexModel.toJsonString());
            System.out.println(res);
            Assert.assertEquals(res.get("code"), expectedReturnCode.value());
            if(expectedResponse == null)
                Assert.assertNull(res.get("response"));
            else
                Assert.assertEquals(encoder.encodeJsonString(res.get("response").toString()), encoder.encodeJsonString(expectedResponse));
        } catch (Exception ex) {
            Assert.fail("Test create index failed!!!", ex);
        }
    }


}
