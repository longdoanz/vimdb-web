package com.viettel.imdb.rest.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.viettel.imdb.common.Record;
import com.viettel.imdb.util.CBOREncodeDecoderNew;
import com.viettel.imdb.util.IMDBEncodeDecoder;

import java.io.IOException;

/**
 * @author longdt20
 * @since 04/12/2018.
 */

public class RecordSerializer extends JsonSerializer<Record> {

    public static final IMDBEncodeDecoder encodeDecoder = new CBOREncodeDecoderNew();

    @Override
    public void serialize(Record record, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        record.getValue().forEach(field -> {
            try {
                JsonNode jsonNode = encodeDecoder.decodeJsonNode(field.getFieldValue());
                jsonGenerator.writeFieldName(field.getFieldName().replace("$.", ""));
                jsonGenerator.writeRawValue(jsonNode.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndObject();
    }
}

