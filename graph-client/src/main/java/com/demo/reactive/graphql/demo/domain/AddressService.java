package com.demo.reactive.graphql.demo.domain;

import com.demo.reactive.graphql.demo.infrastructure.repo.AddressRepository;
import com.demo.reactive.graphql.demo.model.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.List;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;

    private final Sinks.Many<Address> sink = Sinks.many().replay().latest();

    public Mono<Address> addAddress(
            String streetAddressOne,
            String streetAddressTwo,
            String streetAddressThree,
            String city,
            String country) {

        log.info("ADD ADDRESS");

        return addressRepository.save(
                Address.builder()
                        .streetAddressOne(streetAddressOne)
                        .streetAddressTwo(streetAddressTwo)
                        .streetAddressThree(streetAddressThree)
                        .city(city)
                        .country(country)
                        .build())
                .doOnNext(c -> {
                    log.info("Emitting event Address {} - {} created", c.getId(), c.getStreetAddressOne());
                    sink.emitNext(c, Sinks.EmitFailureHandler.FAIL_FAST);
                });
    }

    public Flux<Address> addressAdded() {
        return sink.asFlux()
                .doOnNext(s -> log.info("Subscribers are consuming address with id = {}", s.getId()));
    }

    public Flux<Address> findAll() {
        return addressRepository.findAll();
    }

    public Flux<Address> findAllById(List<Long> addressId) {
        return addressRepository.findAllById(addressId);
    }

    public Mono<Address> findById(long addressId) {
        return addressRepository.findById(addressId);
    }
}
