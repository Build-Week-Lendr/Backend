package com.zero5nelsonm.lendr.config;

import com.fasterxml.classmate.TypeResolver;
import com.zero5nelsonm.lendr.model.ErrorDetail;
import com.zero5nelsonm.lendr.model.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Config {

    @Autowired
    private TypeResolver resolver;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.zero5nelsonm"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false) // Allows only my exception responses
                .ignoredParameterTypes(Pageable.class) // allows only my paging parameter list
                .apiInfo(apiEndPointsInfo())
                .pathMapping("/")
                .additionalModels(resolver.resolve(ErrorDetail.class),
                        resolver.resolve(TokenModel.class))
                .ignoredParameterTypes()
                .tags(new Tag("UserEndpoints",
                                "Endpoints driven by users"),
                        new Tag("ItemEndpoints",
                                "Endpoints driven by roles"),
                        new Tag("LogoutEndpoint",
                                "Endpoint for logging out a user"),
                        new Tag("OpenEndpoint",
                                "Endpoints available to all"));
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Lendr Project")
                .description("Lendr Project Backend")
                .contact(new Contact("Matthew Nelson",
                        "http://www.lambdaschool.com",
                        "mjnelso2@protonmail.com"))
                .license("MIT")
                .licenseUrl("https://github.com/Build-Week-Lendr/Backend/blob/master/LICENSE")
                .version(("1.0.0"))
                .build();
    }
}
