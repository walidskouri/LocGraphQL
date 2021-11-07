package com.demo.reactive.graphql.demo.infrastructure.repo;

import com.demo.reactive.graphql.demo.model.Facility;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 */
public interface FacilityRepository extends ReactiveSortingRepository<Facility, Long> {

    Flux<Facility> findAllByAddressId(long addressId);

    Mono<Facility> findByAddressId(long addressId);

    Flux<Facility> findByName(String name);


}
