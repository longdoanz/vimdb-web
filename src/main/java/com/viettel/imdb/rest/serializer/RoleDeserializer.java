package com.viettel.imdb.rest.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.viettel.imdb.core.security.Role;
import com.viettel.imdb.rest.exception.ExceptionType;
import org.pmw.tinylog.Logger;
import org.springframework.boot.jackson.JsonComponent;

import java.util.ArrayList;
import java.util.List;


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
            System.out.println(jsonNode);
            String name = jsonNode.get("name").asText();

            JsonNode privileges = jsonNode.get("privileges");

            List<String> prvList = new ArrayList<>();

            for (JsonNode obj : privileges) {
                String permission = obj.get("permission").asText();
                JsonNode resourceObj = obj.get("resource");
                String resource = resourceObj.get("name").asText();
                if(resource.equalsIgnoreCase("data")) {
                    prvList.add(String.format("%s.%s.%s", permission, resource, resourceObj.get("table").asText()));
                } else {
                    prvList.add(String.format("%s.%s.%s", permission, resource, resourceObj.get("user").asText()));
                }
            }

//        String[] lst = jsonNode.get("privileges").split("\\.");

            Role role = new Role(name, prvList);
            return role;
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            throw new ExceptionType.ParseError();
        }
    }
}

