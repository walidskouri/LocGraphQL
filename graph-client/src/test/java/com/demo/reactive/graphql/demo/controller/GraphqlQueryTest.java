package com.demo.reactive.graphql.demo.controller;


import com.demo.reactive.graphql.demo.infrastructure.repo.AddressRepository;
import com.demo.reactive.graphql.demo.infrastructure.repo.FacilityRepository;
import com.demo.reactive.graphql.demo.infrastructure.spi.random.RandomApiClient;
import com.demo.reactive.graphql.demo.model.Address;
import com.demo.reactive.graphql.demo.model.Facility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.boot.test.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 */
@GraphQlTest(controllers = {FacilityGraphqlController.class})
class GraphqlQueryTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    FacilityRepository facilityRepository;
    @MockBean
    AddressRepository addressRepository;
    @MockBean
    RandomApiClient randomApiClient;

    @Test
    void getFacilitiesTest() {
        // Given
        when(facilityRepository.findAll()).thenReturn(
                Flux.just(
                        Facility.builder().id(1).anabel("Anabel Mock 1").name("Facility 1").build(),
                        Facility.builder().id(2).anabel("Anabel Mock 2").name("Facility 2").build()
                )
        );

        var query = " { facilities { id anabel name }}";

        // When
        // Then
        graphQlTester.query(query)
                .execute()
                .path("data.facilities[*].name")
                .entityList(String.class).containsExactly("Facility 1", "Facility 2")
                .path("data.facilities[*].id")
                .entityList(String.class).containsExactly("1", "2")
                .path("data.facilities[*].address")
                .valueIsEmpty()

        ;

        verify(facilityRepository, times(1)).findAll();
        verifyNoMoreInteractions(facilityRepository);

    }

    @Test
    void getFacilitiesWithAddressTest() {
        // Given
        when(facilityRepository.findAll()).thenReturn(
                Flux.just(
                        Facility.builder().id(1).anabel("Anabel Mock 1").name("Facility 1").addressId(1).build(),
                        Facility.builder().id(2).anabel("Anabel Mock 2").name("Facility 2").addressId(2).build()
                )
        );
        when(addressRepository.findById(eq(1L))).thenReturn(
                Mono.just(
                        Address.builder().id(1).streetAddressOne("Add1").country("FRANCE").city("PARIS").build()
                )
        );
        when(addressRepository.findById(eq(2L))).thenReturn(
                Mono.just(
                        Address.builder().id(2).streetAddressOne("Add2").country("FRANCE").city("PARIS").build()
                )
        );

        var query = "{ facilities { id anabel name address { id country streetAddressOne } }}";

        // When
        // Then
        graphQlTester.query(query)
                .execute()
                .path("data.facilities[*].name")
                .entityList(String.class).containsExactly("Facility 1", "Facility 2")
                .path("data.facilities[*].id")
                .entityList(String.class).containsExactly("1", "2")
                .path("data.facilities[0].address").pathExists()
                .path("data.facilities[0].address.country").entity(String.class).isEqualTo("FRANCE")
                .path("data.facilities[0].address.streetAddressOne").entity(String.class).isEqualTo("Add1")
                .path("data.facilities[1].address.country").entity(String.class).isEqualTo("FRANCE")
                .path("data.facilities[1].address.streetAddressOne").entity(String.class).isEqualTo("Add2")
                .path("data.facilities[*].address.city").pathDoesNotExist()

        ;

        verify(facilityRepository, times(1)).findAll();
        verify(addressRepository, times(1)).findById(eq(1L));
        verify(addressRepository, times(1)).findById(eq(2L));

        verifyNoMoreInteractions(facilityRepository);

    }

    @Test
    void findFacilityByNameTest() {

        // Given
        when(facilityRepository.findByName(eq("Facility 1"))).thenReturn(
                Flux.just(
                        Facility.builder().id(1).anabel("Anabel Mock 1").name("Facility 1").build(),
                        Facility.builder().id(2).anabel("Anabel Mock 2").name("Facility 1").build()
                )
        );

        var query = "query findFacilityByName($name:String){ findFacilityByName(name:$name) { id anabel name }}";

        // When
        // Then
        graphQlTester.query(query)
                .variable("name", "Facility 1")
                .execute()
                .path("data.findFacilityByName[*].anabel")
                .entityList(String.class).containsExactly("Anabel Mock 1", "Anabel Mock 2")
                .path("data.findFacilityByName[*].id")
                .entityList(String.class).containsExactly("1", "2")
                .path("data.findFacilityByName[*].address")
                .valueIsEmpty();

        verify(facilityRepository, times(1)).findByName(anyString());
        verifyNoMoreInteractions(facilityRepository);

    }


}