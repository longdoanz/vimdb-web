package com.viettel.imdb.rest;

import com.viettel.imdb.IMDBClient;
import com.viettel.imdb.JavaClient;
import com.viettel.imdb.common.ClientConfig;
import com.viettel.imdb.core.security.User;
import io.trane.future.CheckedFutureException;
import org.testng.annotations.Test;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

public class DataTest {


    @Test
    public void testCreateUser() throws CheckedFutureException {
        try {
            IMDBClient client = new JavaClient("172.16.31.55:14080", new ClientConfig(true, "admin", "admin"));
            client.echo(1).get(Duration.ofMinutes(1));
            client.createUser("ldt", "123".getBytes(), new ArrayList<String>() {
                {
                    add("admin");
                }
            }).get(Duration.ofMillis(Integer.MAX_VALUE));
            IMDBClient client2 = new JavaClient("172.16.31.55:14080", new ClientConfig(true, "ldt", "123"));
            User user = client2.readUser("admin").get(Duration.ofMillis(Integer.MAX_VALUE));
            System.out.println("Admin: " + user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void testReadProperty() throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new BufferedInputStream(new FileInputStream("/data/1.Projects/imdb-rest/src/test/resources/default_metrics.properties"));
            Properties properties = new Properties();

            properties.load(new InputStreamReader(inputStream, "UTF-8")); // load all properties in config.properties file
            System.out.println(properties.stringPropertyNames());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }

}
