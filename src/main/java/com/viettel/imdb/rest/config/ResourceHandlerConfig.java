//package com.viettel.imdb.rest.config;
//
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * @author quannh22
// * @since 28/08/2019
// */
//@Configuration
//@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
//@SuppressWarnings("deprecation")
//public class ResourceHandlerConfig extends WebMvcConfigurerAdapter {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/swagger-ui.html")
//                .addResourceLocations("classpath:/resources/swagger-ui.html");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/resources/webjars/");
//    }
//}
