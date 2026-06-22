package com.arnt.notification.events.inbound;

import java.util.UUID;

public record MemberSubscribedEvent(
    UUID memberID,
    String memberEmail,
    UUID subscriptionID
) {}
