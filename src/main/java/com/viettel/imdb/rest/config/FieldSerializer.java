package com.viettel.imdb.rest.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.viettel.imdb.common.Field;
import com.viettel.imdb.util.CBOREncodeDecoderNew;
import com.viettel.imdb.util.IMDBEncodeDecoder;

import java.io.IOException;

/**
 * @author longdt20
 * @since 15:27 05/12/2018
 */


public class FieldSerializer extends JsonSerializer<Field> {

    public static final IMDBEncodeDecoder encodeDecoder = new CBOREncodeDecoderNew();

    @Override
    public void serialize(Field field, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {

            jsonGenerator.writeStartObject();
            JsonNode jsonNode = encodeDecoder.decodeJsonNode(field.getFieldValue());
            jsonGenerator.writeFieldName(field.getFieldName());
            jsonGenerator.writeRawValue(jsonNode.toString());
            jsonGenerator.writeEndObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}