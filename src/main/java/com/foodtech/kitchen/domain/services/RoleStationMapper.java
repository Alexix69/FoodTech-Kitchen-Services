package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.model.Station;
import com.foodtech.kitchen.domain.model.UserRole;

import java.util.Set;

public final class RoleStationMapper {

    private RoleStationMapper() {}

    public static Set<Station> stationsFor(UserRole role) {
        return switch (role) {
            case MESERO    -> Set.of();
            case COCINERO  -> Set.of(Station.HOT_KITCHEN, Station.COLD_KITCHEN);
            case BARTENDER -> Set.of(Station.BAR);
        };
    }
}
