package com.demo.loc.datainitializr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "address")
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "street_address_one")
    private String streetAddressOne;

    @Column(name = "street_address_two")
    private String streetAddressTwo;

    @Column(name = "street_address_three")
    private String streetAddressThree;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

}
