package com.arnt.catalog.events.inbound;

import java.util.UUID;

public record PlanningCancelledEvent(
    UUID planningID
) {}
