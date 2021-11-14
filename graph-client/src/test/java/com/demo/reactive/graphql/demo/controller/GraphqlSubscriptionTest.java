package com.demo.reactive.graphql.demo.controller;

import com.demo.reactive.graphql.demo.domain.AddressService;
import com.demo.reactive.graphql.demo.infrastructure.repo.FacilityRepository;
import com.demo.reactive.graphql.demo.infrastructure.spi.random.RandomApiClient;
import com.demo.reactive.graphql.demo.model.Facility;
import com.demo.reactive.graphql.demo.model.FacilityEvent;
import com.demo.reactive.graphql.demo.model.FacilityEventType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.boot.test.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@GraphQlTest(controllers = {FacilityGraphqlController.class})
@Slf4j
public class GraphqlSubscriptionTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    AddressService addressService;

    @MockBean
    FacilityRepository facilityRepository;

    @MockBean
    RandomApiClient randomApiClient;

    @Test
    void facilityEventsTest() {
        // Given
        when(facilityRepository.findById(eq(1L))).thenReturn(
                Mono.just(
                        Facility.builder().id(1).name("Subscribed Facility").anabel("Anabel for Subscribed Facility").addressId(1L).build()
                )
        );

        String query = "subscription {\n" +
                "            facilityEvents( facilityId : 1, delay : 0) {\n" +
                "                facility { id, name, anabel }\n" +
                "                event\n" +
                "            }\n" +
                "        }";

        // When
        // Then
        graphQlTester.query(query)
                .executeSubscription()
                .toFlux("facilityEvents.facility.name", String.class)
                .as(StepVerifier::create)
                .expectNext("Subscribed Facility")
                .expectNextCount(99)
                .expectComplete()
                .verify();

        graphQlTester.query(query)
                .executeSubscription()
                .toFlux("facilityEvents.event", FacilityEventType.class)
                .as(StepVerifier::create)
                .expectNextMatches(event -> List.of(FacilityEventType.values()).contains(event))
                .expectNextCount(99)
                .expectComplete()
                .verify();

        // Verify the invocation of the mocks.
        verify(facilityRepository, times(2)).findById(eq(1L));
        verifyNoMoreInteractions(facilityRepository);

    }

    @Test
    void facilityEventsTestWithMoreAssertions() {

        // Given
        when(facilityRepository.findById(eq(1L))).thenReturn(
                Mono.just(
                        Facility.builder().id(1L).name("Subscribed Facility").anabel("Anabel for Subscribed Facility").addressId(1L).build()
                )
        );

        String query = "subscription {\n" +
                "            facilityEvents( facilityId : 1, delay : 0) {\n" +
                "                facility { id, name, anabel }\n" +
                "                event\n" +
                "            }\n" +
                "        }";

        // When
        // Then
        graphQlTester.query(query)
                .executeSubscription()
                .toFlux("facilityEvents", FacilityEvent.class)
                .as(StepVerifier::create)
                .consumeNextWith(c ->
                        {
                            assertThat(c.getFacility().getId()).isEqualTo(1);
                            assertThat(c.getFacility().getName()).isEqualTo("Subscribed Facility");
                            assertThat(c.getEvent()).isIn(List.of(FacilityEventType.values()));
                        }
                )
                .thenCancel()
                .verify();

        // Verify the invocation of the mocks.
        verify(facilityRepository, times(1)).findById(eq(1L));
        verifyNoMoreInteractions(facilityRepository);

    }

//    @Test
//    void addressAddedSubscriptionTest() {
//
//        when(addressRepository.save(any()))
//                .thenReturn(Mono.just(Address.builder()
//                        .id(1L)
//                        .city("PARIS")
//                        .country("FRANCE")
//                        .streetAddressThree("add 1")
//                        .build()));
//
//        Flux<Address> result = this.graphQlTester.query("subscription { addressAdded { id city country } }")
//                .executeSubscription()
//                .toFlux("addressAdded", Address.class);
//
//        var verify = StepVerifier.create(result)
//                .consumeNextWith(c -> assertThat(c.getStreetAddressOne()).startsWith("comment of my post at "))
//                .consumeNextWith(c -> assertThat(c.getStreetAddressOne()).startsWith("comment of my post at "))
//                .consumeNextWith(c -> assertThat(c.getStreetAddressOne()).startsWith("comment of my post at "))
//                .thenCancel().verifyLater();
//
//        addAddressToPost("Address One " + UUID.randomUUID().toString(), "PARIS", "FRANCE");
//        addAddressToPost("Address One " + UUID.randomUUID().toString(), "PARIS", "FRANCE");
//        addAddressToPost("Address One " + UUID.randomUUID().toString(), "PARIS", "FRANCE");
//
//        verify.verify();
//        verify(addressRepository, times(3)).save(any());
//    }
//
//    private void addAddressToPost(String addressStreetOne, String city, String country) {
//
//        var addComment = "mutation addAddress($streetAddressOne: String, $city: String, $country: String) " +
//                "{ addAddress(streetAddressOne:$streetAddressOne, city:$city, country:$country) " +
//                "{ id }}";
//
//        Integer addressId = graphQlTester.query(addComment)
//                .variable("streetAddressOne", addressStreetOne)
//                .variable("city", city)
//                .variable("country", country)
//                .execute()
//                .path("addAddress.id")
//                .entity(Integer.class).get();
//
//        log.info("added comment of post: {}", addressId);
//        assertThat(addressId).isNotNull();
//
//    }
}
