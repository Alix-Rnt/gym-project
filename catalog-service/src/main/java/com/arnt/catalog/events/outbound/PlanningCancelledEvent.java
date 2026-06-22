package com.arnt.catalog.events.outbound;

import java.util.UUID;

public record PlanningCancelledEvent(
    UUID planningID
) {}
