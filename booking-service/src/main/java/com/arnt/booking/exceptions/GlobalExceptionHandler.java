package com.arnt.booking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SubscriptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleSubscriptionNotFound() {}

    @ExceptionHandler(WaitlistNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleWaitlistNotFound() {}

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleMemberNotFound() {}

    @ExceptionHandler(PlanningNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePlanningNotFound() {}

    @ExceptionHandler(WaitlistEmptyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleWaitlistEmpty() {}
}