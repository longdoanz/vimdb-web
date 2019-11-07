package com.viettel.imdb.rest.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.rest.serializer.RecordSerializer;
import com.viettel.imdb.rest.serializer.RoleSerializer;
import org.springframework.stereotype.Service;

/**
 * @author longdt20
 * @since 04/12/2018.
 */

@Service
public class JsonModule extends SimpleModule {

    public JsonModule() {
        this.addSerializer(Record.class, new RecordSerializer());
        this.addSerializer(Role.class, new RoleSerializer());
    }
}
