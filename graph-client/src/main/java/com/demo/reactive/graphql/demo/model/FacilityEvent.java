package com.demo.reactive.graphql.demo.model;

import lombok.Builder;
import lombok.Data;

/**
 *
 */
@Data
@Builder
public class FacilityEvent {

    private Facility facility;

    private FacilityEventType event;

}
