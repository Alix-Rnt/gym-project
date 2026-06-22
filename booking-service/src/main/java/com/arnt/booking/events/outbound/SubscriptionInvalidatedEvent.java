package com.arnt.booking.events.outbound;

import java.util.List;
import java.util.UUID;

public record SubscriptionInvalidatedEvent(
    UUID subscriptionId,
    String subscriptionName,
    List<UUID> membersId
) {}
