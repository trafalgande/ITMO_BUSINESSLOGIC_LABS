package se.ifmo.pepe.lab1.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/error").negate())
                .build()
                .apiInfo(metadata())
                .useDefaultResponseMessages(false)
                .genericModelSubstitutes(Optional.class);

    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Business Logic. 1st lab")
                .description("A. Chaika, A. Changalidi")
                .version("1.0.0")//
                .build();
    }
}
