package com.arnt.notification.events.inbound;

import java.util.UUID;

public record MemberUnsubscribedEvent(
    UUID memberID,
    String memberEmail,
    UUID subscriptionID
) {}
