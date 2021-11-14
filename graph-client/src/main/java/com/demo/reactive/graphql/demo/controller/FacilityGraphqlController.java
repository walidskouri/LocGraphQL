package com.demo.reactive.graphql.demo.controller;

import com.demo.reactive.graphql.demo.domain.AddressService;
import com.demo.reactive.graphql.demo.infrastructure.repo.FacilityRepository;
import com.demo.reactive.graphql.demo.infrastructure.spi.random.RandomApiClient;
import com.demo.reactive.graphql.demo.infrastructure.spi.random.model.RandomDogImageResult;
import com.demo.reactive.graphql.demo.model.Address;
import com.demo.reactive.graphql.demo.model.Contact;
import com.demo.reactive.graphql.demo.model.Facility;
import com.demo.reactive.graphql.demo.model.FacilityEvent;
import com.demo.reactive.graphql.demo.model.FacilityEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FacilityGraphqlController {

    private final FacilityRepository facilityRepository;
    private final AddressService addressService;
    private final RandomApiClient randomApiClient;

    // @SchemaMapping(typeName = "Query", field = "facilities")
    // @QueryMapping(name = "facilities")
    @QueryMapping
    Flux<Facility> facilities() {
        return this.facilityRepository.findAll();
    }

    @SchemaMapping(typeName = "Facility")
    Flux<Address> addresses(Facility facility) {
        return this.addressService.findAllById(List.of(facility.getAddressId()));
    }

    @SchemaMapping(typeName = "Facility")
    Mono<RandomDogImageResult> image(Facility facility) {
        log.info("Looking for image of Facility {}", facility.getAnabel());
        return randomApiClient.getRandomImage((int) facility.getId());
    }

    @SchemaMapping(typeName = "Facility")
    Mono<Address> address(Facility facility) {

        return this.addressService.findById(facility.getAddressId());
    }


    @QueryMapping
    Mono<Page<Facility>> pagedFacilities(@Argument int page, @Argument int size) {
        PageRequest pageable = PageRequest.ofSize(size).withPage(page).withSort(Sort.Direction.ASC, "id");
        return this.facilityRepository.count()
                .flatMap(userCount -> this.facilityRepository.findAll(pageable.getSort())
                        .buffer(pageable.getPageSize(), (pageable.getPageNumber() - 1))
                        .elementAt(pageable.getPageNumber(), new ArrayList<>())
                        .map(facilities -> new PageImpl<>(facilities, pageable, userCount))
                );

    }

    @QueryMapping
    Flux<RandomDogImageResult> randomImages() {
        return Flux.fromStream(IntStream.range(0, 5).boxed())
                .flatMap(randomApiClient::getRandomImage);
    }

    @QueryMapping
    Mono<RandomDogImageResult> randomImage() {
        return randomApiClient.getRandomImage(1);
    }

    @QueryMapping
    Flux<Address> addresses() {
        return this.addressService.findAll();
    }

    @QueryMapping
    Flux<Facility> findFacilityByName(@Argument String name) {
        return facilityRepository.findByName(name);
    }

    @SchemaMapping(typeName = "Address")
    Mono<Facility> facility(Address address) {
        return this.facilityRepository.findByAddressId(address.getId());
    }

    @SchemaMapping(typeName = "Address")
    Flux<Facility> facilities(Address address) {
        return this.facilityRepository.findAllByAddressId(address.getId());
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
        ).delayElement(Duration.ofSeconds(1));

    }

    //@SchemaMapping(typeName = "Mutation", field = "addAddress")
    @MutationMapping
    Mono<Address> addAddress(@Argument String streetAddressOne, @Argument String city, @Argument String country) {
        return addressService.addAddress(streetAddressOne, null, null, city, country);
    }

    @MutationMapping
    Mono<Facility> addFacility(@Argument String name) {
        Facility newAddress = Facility.builder()
                .name(name)
                .anabel(UUID.randomUUID().toString())
                .addressId(new Random().nextInt(50))
                .build();
        return facilityRepository.save(newAddress);
    }

    @SubscriptionMapping
    Flux<FacilityEvent> facilityEvents(@Argument long facilityId, @Argument int delay) {
        return this.facilityRepository.findById(facilityId)
                .flatMapMany(facility -> {
                    Stream<FacilityEvent> stream = Stream.generate(
                            () -> FacilityEvent.builder()
                                    .facility(facility)
                                    .event(FacilityEventType.values()[new Random().nextInt(FacilityEventType.values().length)])
                                    .build()
                    );
                    return Flux.fromStream(stream);
                })
                .delayElements(Duration.ofSeconds(delay))
                .take(100);
    }


    @SubscriptionMapping
    Flux<Address> addressAdded() {
        return addressService.addressAdded();
    }


}
