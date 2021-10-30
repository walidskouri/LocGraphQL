package com.demo.loc.datainitializr.repository;

import com.demo.loc.datainitializr.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


/**
 *
 */
public interface FacilityRepository extends JpaRepository<Facility, Long> {

}
