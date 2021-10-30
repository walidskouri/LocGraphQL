package com.demo.reactive.graphql.demo.controller;

import com.demo.reactive.graphql.demo.model.Address;
import com.demo.reactive.graphql.demo.model.Facility;
import com.demo.reactive.graphql.demo.repo.AddressRepository;
import com.demo.reactive.graphql.demo.repo.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@Controller
@RequiredArgsConstructor
public class FacilityGraphqlController {

    private final FacilityRepository facilityRepository;
    private final AddressRepository addressRepository;

    // @SchemaMapping(typeName = "Query", field = "facilities")
    // @QueryMapping(name = "facilities")
    @QueryMapping
    Flux<Facility> facilities() {
        return this.facilityRepository.findAll();
    }

    @SchemaMapping(typeName = "Facility")
    Flux<Address> addresses(Facility facility) {
        return this.addressRepository.findAllById(List.of(facility.getAddressId()));
    }

    @SchemaMapping(typeName = "Facility")
    Mono<Address> address(Facility facility) {
        return this.addressRepository.findById(facility.getAddressId());
    }

}
