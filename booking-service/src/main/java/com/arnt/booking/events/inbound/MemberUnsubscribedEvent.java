package com.arnt.booking.events.inbound;

import java.util.UUID;

public record MemberUnsubscribedEvent(
    UUID memberID,
    String memberEmail,
    UUID subscriptionID
) {}
