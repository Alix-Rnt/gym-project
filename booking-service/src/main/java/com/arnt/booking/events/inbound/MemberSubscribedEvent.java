package com.arnt.booking.events.inbound;

import java.util.UUID;

public record MemberSubscribedEvent(
    UUID memberID,
    UUID subscriptionID
) {}
