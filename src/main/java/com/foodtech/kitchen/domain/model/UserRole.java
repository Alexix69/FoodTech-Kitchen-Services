package com.foodtech.kitchen.domain.model;

/**
 * Operational roles in the FoodTech system.
 *
 * <ul>
 *   <li><b>MESERO</b> — Waiter; creates orders and manages the order dashboard.
 *       Has no station assignment; never sees kitchen or bar tasks.</li>
 *   <li><b>COCINERO</b> — Kitchen operator; processes tasks at
 *       {@code HOT_KITCHEN} and {@code COLD_KITCHEN} stations.</li>
 *   <li><b>BARTENDER</b> — Bar operator; processes tasks at the {@code BAR} station.</li>
 * </ul>
 *
 * This enum is a pure domain type — no framework annotations of any kind.
 */
public enum UserRole {
    /**
     * Waiter role. Maps to zero stations. Creates orders; does not process tasks.
     */
    MESERO,

    /**
     * Kitchen operator. Maps to {@code HOT_KITCHEN} and {@code COLD_KITCHEN}.
     */
    COCINERO,

    /**
     * Bar operator. Maps to {@code BAR} only.
     */
    BARTENDER
}
