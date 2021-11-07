package com.demo.reactive.graphql.demo.infrastructure.repo;

import com.demo.reactive.graphql.demo.model.Address;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 *
 */
public interface AddressRepository extends ReactiveCrudRepository<Address, Long> {

}
