package com.demo.reactive.graphql.demo.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 *
 */
@Data
@Builder
public class Contact {

    private UUID id;

    private String name;

}
