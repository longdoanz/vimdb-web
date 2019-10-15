package com.viettel.imdb.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@RestController
@RequestMapping("")
@ApiIgnore
public class BaseController {
    @RequestMapping(value = "")
    public void respond() {
    }
}
