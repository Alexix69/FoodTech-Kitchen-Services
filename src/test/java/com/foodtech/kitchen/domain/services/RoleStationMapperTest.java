package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.model.Station;
import com.foodtech.kitchen.domain.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class RoleStationMapperTest {

    @Test
    @DisplayName("MESERO maps to empty station set")
    void meseroMapsToEmptySet() {
        Set<Station> stations = RoleStationMapper.stationsFor(UserRole.MESERO);
        assertNotNull(stations);
        assertTrue(stations.isEmpty(), "MESERO has no assigned stations");
    }

    @Test
    @DisplayName("COCINERO maps to HOT_KITCHEN and COLD_KITCHEN")
    void cocineroMapsToKitchenStations() {
        Set<Station> stations = RoleStationMapper.stationsFor(UserRole.COCINERO);
        assertEquals(Set.of(Station.HOT_KITCHEN, Station.COLD_KITCHEN), stations);
    }

    @Test
    @DisplayName("BARTENDER maps to BAR only")
    void bartenderMapsToBar() {
        Set<Station> stations = RoleStationMapper.stationsFor(UserRole.BARTENDER);
        assertEquals(Set.of(Station.BAR), stations);
    }

    @Test
    @DisplayName("Returned sets are immutable (no Spring annotations in mapper)")
    void returnedSetIsUnmodifiable() {
        Set<Station> stations = RoleStationMapper.stationsFor(UserRole.COCINERO);
        assertThrows(UnsupportedOperationException.class,
                () -> stations.add(Station.BAR));
    }
}
