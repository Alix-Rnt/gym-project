package com.arnt.booking.events.outbound;

import java.util.List;
import java.util.UUID;

public record SubscriptionInvalidatedEvent(
    UUID subscriptionId,
    List<UUID> membersId
) {}
