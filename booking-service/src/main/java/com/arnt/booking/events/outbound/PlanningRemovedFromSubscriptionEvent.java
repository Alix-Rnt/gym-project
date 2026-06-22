package com.arnt.booking.events.outbound;

import java.util.List;
import java.util.UUID;

public record PlanningRemovedFromSubscriptionEvent(
    UUID subscriptionId,
    UUID planningId,
    List<UUID> membersId
) {}
