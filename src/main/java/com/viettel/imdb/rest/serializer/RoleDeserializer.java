package com.viettel.imdb.rest.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.rest.exception.ExceptionType;
import org.pmw.tinylog.Logger;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.viettel.imdb.rest.serializer.RoleSerializer.*;


/**
 * @author longdt20
 * @since 15:50 05/12/2018
 */

@JsonComponent
public class RoleDeserializer extends JsonDeserializer<Role> {
    @Override
    public Role deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {


        try {
            ObjectCodec objectCodec = jsonParser.getCodec();
            JsonNode jsonNode = objectCodec.readTree(jsonParser);

            String name = jsonNode.get(ROLE_NAME_FIELD) == null ? "" : jsonNode.get(ROLE_NAME_FIELD).asText();

            JsonNode privileges = jsonNode.get(PRIVILEGES_FIELD);

            List<String> prvList = new ArrayList<>();

            for (JsonNode obj : privileges) {
                String permission = obj.get(PERMISSION_FIELD).asText();
                JsonNode resourceObj = obj.get(RESOURCE_FIELD);
                String resource = resourceObj.get(RESOURCE_NAME_FIELD).asText();
                if(resource.equalsIgnoreCase(DATA_FIELD)) {
                    prvList.add(String.format("%s.%s.%s", permission, resource, resourceObj.get(TABLE_FIELD).asText()));
                } else {
                    prvList.add(String.format("%s.%s.%s", permission, resource, resourceObj.get(USER_FIELD).asText()));
                }
            }

            return new Role(name, prvList);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            throw new ExceptionType.ParseError();
        }
    }
}

