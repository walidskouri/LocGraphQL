package com.demo.reactive.graphql.demo.infrastructure.spi.random.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "random-api")
public class RandomApiSettings {

    private String baseUri;
}
