package com.demo.reactive.graphql.demo.controller;

import com.demo.reactive.graphql.demo.model.Address;
import com.demo.reactive.graphql.demo.model.Contact;
import com.demo.reactive.graphql.demo.model.Facility;
import com.demo.reactive.graphql.demo.repo.AddressRepository;
import com.demo.reactive.graphql.demo.repo.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FacilityGraphqlController {

    private final FacilityRepository facilityRepository;
    private final AddressRepository addressRepository;

    // @SchemaMapping(typeName = "Query", field = "facilities")
    // @QueryMapping(name = "facilities")
    @QueryMapping
    Flux<Facility> facilities() {
        return this.facilityRepository.findAll();
    }

    @QueryMapping
    Flux<Address> addresses() {
        return this.addressRepository.findAll();
    }

    @QueryMapping
    Flux<Facility> findFacilityByName(@Argument String name) {
        return facilityRepository.findByName(name);
    }

    @SchemaMapping(typeName = "Facility")
    Flux<Address> addresses(Facility facility) {
        return this.addressRepository.findAllById(List.of(facility.getAddressId()));
    }

    @SchemaMapping(typeName = "Facility")
    Mono<Address> address(Facility facility) {
        return this.addressRepository.findById(facility.getAddressId());
    }

    @SchemaMapping(typeName = "Address")
    Mono<Facility> facility(Address address) {
        return this.facilityRepository.findByAddressId(address.getId());
    }

    @SchemaMapping(typeName = "Facility")
    Mono<Contact> contact(Facility facility) {
        log.info("Getting contact for facility {}", facility.getId());
        // Can be a remote call to another service via WebClient for example
        return Mono.just(
                Contact.builder()
                        .id(UUID.randomUUID())
                        .name("Nom contact pour facility " + facility.getName())
                        .build()
        );

    }

    //@SchemaMapping(typeName = "Mutation", field = "addAddress")
    @MutationMapping
    Mono<Address> addAddress(@Argument String streetAddressOne, @Argument String city, @Argument String country) {
        Address newAddress = Address.builder()
                .streetAddressOne(streetAddressOne)
                .city(city)
                .country(country)
                .build();
        return addressRepository.save(newAddress);
    }


}
