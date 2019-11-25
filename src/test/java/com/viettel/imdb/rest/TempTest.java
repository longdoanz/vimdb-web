package com.viettel.imdb.rest;

import org.testng.annotations.Test;

public class TempTest {
    @Test
    public void test01234() {
        String body = "{" +
                "\"name\": \"{0}\"" +
                "}";
        System.out.println(body.replaceAll("\\{0}", "ldt"));
    }
}
