package com.demo.loc.datainitializr.rest;

import com.demo.loc.datainitializr.model.Facility;
import com.demo.loc.datainitializr.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Slf4j
public class FacilityController {

    private final FacilityRepository facilityRepository;

    @GetMapping(value = "/facilities")
    List<Facility> getFacilities() {
        return facilityRepository.findAll();
    }

}
