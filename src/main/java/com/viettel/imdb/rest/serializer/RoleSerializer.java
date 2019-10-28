package com.viettel.imdb.rest.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.util.IMDBEncodeDecoder;

import java.io.IOException;
import java.util.List;

/**
 * @author longdt20
 * @since 04/12/2018.
 */

public class RoleSerializer extends JsonSerializer<Role> {

    public static final IMDBEncodeDecoder encodeDecoder = IMDBEncodeDecoder.getInstance();

    @Override
    public void serialize(Role role, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("roleName");
        jsonGenerator.writeString(role.getRolename());
        jsonGenerator.writeFieldName("privileges");
        jsonGenerator.writeStartArray();

        for(String privilege : role.getPrivilegeList()) {
            String[] lst = privilege.split("\\.");

            if(lst.length > 0) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeFieldName("permission");
                jsonGenerator.writeString(lst[0]);
            }

            if(lst.length > 1) {
                jsonGenerator.writeFieldName("resource");
                jsonGenerator.writeStartObject();
                jsonGenerator.writeFieldName("name");
                jsonGenerator.writeString(lst[1]);
                if(lst.length == 3) {
                    if(lst[1].equalsIgnoreCase("data")) {
                        jsonGenerator.writeFieldName("namespace");
                        jsonGenerator.writeString("namespace");
                        jsonGenerator.writeFieldName("table");
                        jsonGenerator.writeString(lst[2]);
                    } else {
                        jsonGenerator.writeFieldName("user");
                        jsonGenerator.writeString(lst[2]);
                    }
                }
                jsonGenerator.writeEndObject();
            }
            if(lst.length > 0)
                jsonGenerator.writeEndObject();

        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        /*
        role.getValue().forEach(field -> {
            try {
                JsonNode jsonNode = encodeDecoder.decodeJsonNode(field.getFieldValue());
                jsonGenerator.writeFieldName(field.getFieldName().replace("$.", ""));
                jsonGenerator.writeRawValue(jsonNode.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndObject();*/
    }
}

