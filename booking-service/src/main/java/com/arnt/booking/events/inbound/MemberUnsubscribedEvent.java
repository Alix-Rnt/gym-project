package com.arnt.booking.events.inbound;

import java.util.UUID;

public record MemberUnsubscribedEvent(
    UUID memberID,
    UUID subscriptionID
) {}
