package com.demo.loc.datainitializr.init;

import com.demo.loc.datainitializr.model.Address;
import com.demo.loc.datainitializr.model.Facility;
import com.demo.loc.datainitializr.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@Component
@Profile(value = "!test")
@RequiredArgsConstructor
@Slf4j
public class DataAppInitializr {

    private final FacilityRepository facilityRepository;
    private static final List<String> cities = List.of("PARIS", "TOULOUSE", "LYON", "LILLE", "BORDEAUX");

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        Date start = new Date();
        facilityRepository.deleteAll();
        facilityRepository.saveAll(generateRandomFacilities());
        log.info("Database Initialisation done in {}ms", Duration.between(start.toInstant(), new Date().toInstant()).toMillis());
    }

    private List<Facility> generateRandomFacilities() {
        return IntStream.range(1, 100)
                .mapToObj(i -> Facility.builder()
                        .name("Facility " + i)
                        .anabel(UUID.randomUUID().toString())
                        .address(Address
                                .builder()
                                .city(cities.get((new Random().nextInt(cities.size()))))
                                .build())
                        .build()
                )
                .map(Facility.class::cast)
                .collect(Collectors.toList());
    }

}
