package com.demo.reactive.graphql.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;


/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@Data
public class Address {

    @Id
    private long id;

    @Column("street_address_one")
    private String streetAddressOne;

    @Column("street_address_two")
    private String streetAddressTwo;

    @Column("street_address_three")
    private String streetAddressThree;

    @Column("city")
    private String city;

    @Column("country")
    private String country;
}
