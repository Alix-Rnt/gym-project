package com.arnt.booking.events.inbound;

import java.util.UUID;

public record PlanningCancelledEvent(
    UUID planningID
) {}
