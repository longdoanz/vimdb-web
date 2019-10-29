package com.viettel.imdb.rest.common;

public class JsonGenerator {
    public StringBuilder jsonString;

    private boolean addComma = false;

    public JsonGenerator() {
        jsonString = new StringBuilder();
    }

    public static JsonGenerator createObject() {
        JsonGenerator jsonGenerator = new JsonGenerator();
        jsonGenerator.writeStartObject();
        return jsonGenerator;
    }

    public JsonGenerator writeStartObject() {
        addComma = false;
        jsonString.append("{\n");
        return this;
    }

    public JsonGenerator writeEndObject() {
        addComma = true;
        jsonString.append("\n}");
        return this;
    }

    private void beforeAppendElement() {
        if(addComma) {
            jsonString.append(",\n");
        }
    }

    public JsonGenerator write(String key, String value) {
        beforeAppendElement();
        jsonString.append(String.format("\"%s\": \"%s\"", key, value));
        return this;
    }

    public JsonGenerator write(String key, long value) {
        jsonString.append(String.format("\"%s\": %d", key, value));
        return this;
    }

    public JsonGenerator write(String key, float value) {
        jsonString.append(String.format("\"%s\": %f", key, value));
        return this;
    }

}
