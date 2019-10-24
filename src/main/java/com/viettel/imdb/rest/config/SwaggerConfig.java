package com.viettel.imdb.rest.config;


import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.viettel.imdb.rest.domain.RestClientError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author quannh22
 * @since 08/08/2019
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TypeResolver typeResolver;

    private ApiInfo apiInfo() {
        ApiInfo info = new ApiInfoBuilder()
                .version("1.0")
                .title("vIMDB REST Client")
                .description("REST Interface for vIMDB.")
                .contact(new Contact("Viettel, Inc.", "https://www.viettel.com.vn", ""))
                .build();
        return info;
    }

    @Bean
    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(regex("/error*"))) // We don't want the base error controller listed.
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, default500Error())
                .globalResponseMessage(RequestMethod.POST, default500Error())
                .globalResponseMessage(RequestMethod.PATCH, default500Error())
                .globalResponseMessage(RequestMethod.DELETE, default500Error())
                .globalResponseMessage(RequestMethod.HEAD, default500Error())
                .globalResponseMessage(RequestMethod.PUT, default500Error())
                .additionalModels(typeResolver.resolve(RestClientError.class))
//                .securityContexts(Lists.newArrayList(securityContext()))
//                .securitySchemes(Lists.newArrayList(apiKey()))
                .alternateTypeRules(alternateRules());
    }

    private AlternateTypeRule[] alternateRules() {
        return new AlternateTypeRule[] {
                AlternateTypeRules.newRule(
                        typeResolver.resolve(List.class, typeResolver.resolve(Map.class, String.class, Object.class)),
                        typeResolver.resolve(List.class, Object.class))
        };
    }

    private List<springfox.documentation.service.ResponseMessage> default500Error() {
        return new ArrayList<springfox.documentation.service.ResponseMessage>(){{
            add(new ResponseMessageBuilder()
                    .code(401)
                    .message("Unauthorized")
                    .build()
            );
            add(new ResponseMessageBuilder()
                    .code(500)
                    .responseModel(new ModelRef("RestClientError"))
                    .message("The REST Client encountered an error processing the request")
                    .build()
            );
        }};
    }


    private ApiKey apiKey() {
        return new ApiKey("Bearer-JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(regex("/v1/.*"))
                .build();
    }

     List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference("Bearer-JWT", authorizationScopes));
    }
}
