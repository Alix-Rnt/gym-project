package com.arnt.booking.events.outbound;

import java.util.UUID;

public record WaitlistPromotedEvent(
    UUID waitlistID,
    UUID memberID,
    String subscriptionName
) {}
