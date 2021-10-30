package com.demo.reactive.graphql.demo.model;

import lombok.Builder;
import lombok.Data;

/**
 * 9fbef606107a605d69c0edbcd8029e5d
 */
@Data
@Builder
public class FacilityEvent {

    private Facility facility;

    private FacilityEventType event;

}
