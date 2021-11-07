package com.demo.reactive.graphql.demo.infrastructure.spi.random.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

/**
 *
 */
@Value
@Builder
@JsonDeserialize(builder = RandomDogImageResult.RandomDogImageResultBuilder.class)
public class RandomDogImageResult {

    @JsonProperty
    String message;

    @JsonProperty
    String status;

}
