package com.demo.reactive.graphql.demo.repo;

import com.demo.reactive.graphql.demo.model.Facility;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 */
public interface FacilityRepository extends ReactiveCrudRepository<Facility, Long> {

    Mono<Facility> findByAddressId(long addressId);

    Flux<Facility> findByName(String name);

}
