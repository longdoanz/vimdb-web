package com.viettel.imdb.rest.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.rest.serializer.RecordSerializer;
import org.springframework.stereotype.Service;

/**
 * @author longdt20
 * @since 04/12/2018.
 */

@Service
public class JsonModule extends SimpleModule {

    public JsonModule() {
        this.addSerializer(Record.class, new RecordSerializer());
//        this.addSerializer(Field.class, new FieldSerializer());
    }
}
