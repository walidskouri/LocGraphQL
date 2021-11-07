package com.demo.reactive.graphql.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

/**
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Facility {

    @Id
    private long id;

    private String anabel;

    private String name;

    @Column("address_fk")
    private long addressId;

}
