package com.demo.reactive.graphql.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
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
