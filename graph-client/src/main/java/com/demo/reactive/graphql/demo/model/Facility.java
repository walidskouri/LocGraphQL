package com.demo.reactive.graphql.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 *
 */
@Data
public class Facility {

    @Id
    private long id;

    private String anabel;

    private String name;

    @Column("address_fk")
    private long addressId;

}
