package com.junzixiehui.wwx.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


@Slf4j
@SpringBootApplication(
		scanBasePackages = {"com.junzixiehui.wwx"},
		exclude = {DataSourceAutoConfiguration.class,
				MongoAutoConfiguration.class,
				ElasticsearchAutoConfiguration.class}
)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
