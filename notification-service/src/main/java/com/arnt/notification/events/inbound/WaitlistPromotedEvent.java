package com.arnt.notification.events.inbound;

import java.util.UUID;

public record WaitlistPromotedEvent(
    UUID memberID,
    UUID subscriptionID,
    String subscriptionName
) {}
