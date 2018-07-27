package com.junzixiehui.wwx.web.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

/**
 * <p>Description: Swagger配置
 *   本地访问 http://localhost:8112/swagger-ui.html
 *   参考：http://einverne.github.io/post/2017/10/swagger-with-spring-boot.html
 * </p>
 *
 * @author: by qulibin
 * @date: 2017/12/13  10:34
 * @version: 1.0
 */
@Configuration
@EnableSwagger2
@Profile({"testing","development","production"})
public class SwaggerConfig {


    @Bean
    public Docket createRestApi() {
        return new Docket(SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.junzixiehui.wwx.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("无尾熊家装 APIs")
                .description("人人车售后订单中心 对外提供服务APIs")
                .contact("962516538@qq.com")
                .version("1.0")
                .build();
    }


   /* @Bean
    public Docket customImplementation(){
        boolean enable = true;
        return new Docket(SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable);
    }*/



}
