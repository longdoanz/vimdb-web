package com.viettel.imdb.rest.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.util.IMDBEncodeDecoder;

import java.io.IOException;

/**
 * @author longdt20
 * @since 04/12/2018.
 */

public class RoleSerializer extends JsonSerializer<Role> {

    static final String ROLE_NAME_FIELD = "name";
    static final String PRIVILEGES_FIELD = "privileges";
    static final String PERMISSION_FIELD = "permission";
    static final String RESOURCE_FIELD = "resource";
    static final String RESOURCE_NAME_FIELD = "name";
    private static final String NAMESPACE_FIELD = "namespace";
    static final String TABLE_FIELD = "table";
    static final String DATA_FIELD = "data";
    static final String USER_FIELD = "user";

    @Override
    public void serialize(Role role, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName(ROLE_NAME_FIELD);
        jsonGenerator.writeString(role.getRolename());
        jsonGenerator.writeFieldName(PRIVILEGES_FIELD);
        jsonGenerator.writeStartArray();

        for(String privilege : role.getPrivilegeList()) {
            String[] lst = privilege.split("\\.");

            if(lst.length > 0) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeFieldName(PERMISSION_FIELD);
                jsonGenerator.writeString(lst[0]);
            }

            if(lst.length > 1) {
                jsonGenerator.writeFieldName(RESOURCE_FIELD);
                jsonGenerator.writeStartObject();
                jsonGenerator.writeFieldName(RESOURCE_NAME_FIELD);
                jsonGenerator.writeString(lst[1]);
                if(lst.length == 3) {
                    if(lst[1].equalsIgnoreCase(DATA_FIELD)) {
                        jsonGenerator.writeFieldName(NAMESPACE_FIELD);
                        jsonGenerator.writeString("NAMESPACE");
                        jsonGenerator.writeFieldName(TABLE_FIELD);
                    } else {
                        jsonGenerator.writeFieldName(USER_FIELD);
                    }
                    jsonGenerator.writeString(lst[2]);
                }
                jsonGenerator.writeEndObject();
            }
            if(lst.length > 0)
                jsonGenerator.writeEndObject();

        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}

