package com.arnt.booking.events.inbound;

import java.util.UUID;

public record MemberSubscribedEvent(
    UUID memberID,
    String memberEmail,
    UUID subscriptionID
) {}
