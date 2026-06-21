package com.arnt.notification.events.inbound;

import java.util.List;
import java.util.UUID;

public record SubscriptionInvalidatedEvent(
    UUID subscriptionID,
    String subscriptionName,
    List<UUID> membersID
) {}
