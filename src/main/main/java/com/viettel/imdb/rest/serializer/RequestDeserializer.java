/*
package com.viettel.imdb.rest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

*/
/**
 * @author longdt20
 * @since 15:50 05/12/2018
 *//*


public class RequestDeserializer extends JsonDeserializer<RecordModel> {
    @Override
    public RecordModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode jsonNode = objectCodec.readTree(jsonParser);
        System.out.println(jsonNode);
        RecordModel records = new RecordModel();
        return records;
    }
}
*/
