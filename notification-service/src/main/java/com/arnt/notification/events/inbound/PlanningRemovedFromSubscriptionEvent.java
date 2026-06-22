package com.arnt.notification.events.inbound;

import java.util.List;
import java.util.UUID;

public record PlanningRemovedFromSubscriptionEvent(
    UUID subscriptionID,
    UUID planningID,
    List<UUID> membersID
) {}
