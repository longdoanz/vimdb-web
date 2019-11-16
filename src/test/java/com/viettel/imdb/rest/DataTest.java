package com.viettel.imdb.rest;

import com.linkedin.paldb.api.PalDB;
import com.linkedin.paldb.api.StoreReader;
import com.linkedin.paldb.api.StoreWriter;
import com.viettel.imdb.rest.common.HTTPRequest;
import org.pmw.tinylog.Logger;
import org.testng.annotations.Test;

import java.io.File;

public class DataTest {
    private HTTPRequest http;

    @Test void getDataInfitest() throws Exception {
        // Open a db with default options.
        StoreWriter writer = PalDB.createWriter(new File("store.paldb"));
        writer.put("foo", "bar");
        writer.put(1213, new int[] {1, 2, 3});
        writer.close();
        StoreReader reader = PalDB.createReader(new File("store.paldb"));
        String val1 = reader.get("foo");
        int[] val2 = reader.get(1213);
        Logger.info("Value: {} value 2: {}", val1, val2);
        reader.close();
    }

}
