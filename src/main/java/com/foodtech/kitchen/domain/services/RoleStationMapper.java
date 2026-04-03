package com.foodtech.kitchen.domain.services;

import com.foodtech.kitchen.domain.model.Station;
import com.foodtech.kitchen.domain.model.UserRole;

import java.util.Set;

/**
 * Domain service that maps each {@link UserRole} to the set of {@link Station}s
 * that role is responsible for.
 *
 * <p>This is the single source of truth for role-to-station mapping.
 * It is intentionally a {@code final} utility class with a private constructor and
 * only static methods — no Spring beans, no framework annotations.
 *
 * <p>Mapping rules:
 * <ul>
 *   <li>MESERO   → empty set (waiters have no station)</li>
 *   <li>COCINERO → {HOT_KITCHEN, COLD_KITCHEN}</li>
 *   <li>BARTENDER → {BAR}</li>
 * </ul>
 */
public final class RoleStationMapper {

    private RoleStationMapper() {
        // Utility class — do not instantiate
    }

    /**
     * Returns the immutable set of {@link Station}s accessible to the given {@code role}.
     *
     * @param role the operational role; must not be {@code null}
     * @return an unmodifiable set of allowed stations (may be empty)
     */
    public static Set<Station> stationsFor(UserRole role) {
        return switch (role) {
            case MESERO    -> Set.of();
            case COCINERO  -> Set.of(Station.HOT_KITCHEN, Station.COLD_KITCHEN);
            case BARTENDER -> Set.of(Station.BAR);
        };
    }
}
