package com.demo.reactive.graphql.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FacilityEvent {

    private Facility facility;

    private FacilityEventType event;

}
