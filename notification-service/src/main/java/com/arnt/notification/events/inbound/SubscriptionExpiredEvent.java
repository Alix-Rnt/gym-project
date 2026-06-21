package com.arnt.notification.events.inbound;

import java.util.List;
import java.util.UUID;

public record SubscriptionExpiredEvent(
    UUID subscriptionID,
    String subscriptionName,
    List<UUID> membersID
) {}
