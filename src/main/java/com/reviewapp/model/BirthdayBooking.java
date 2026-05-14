package com.reviewapp.model;

/** BirthdayBooking – OOP INHERITANCE from Booking. */
public class BirthdayBooking extends Booking {

    public BirthdayBooking() { super(); }

    public BirthdayBooking(String id, String customerId, String customerName,
                           String packageId, String packageName,
                           String eventDate, String status, String notes) {
        super(id, customerId, customerName, packageId, packageName, eventDate, status, notes);
    }

    @Override
    public String getEventType() { return "BIRTHDAY"; }

    @Override
    public String getBookingRule() {
        return "Birthday bookings must be made at least 7 days in advance.";
    }
}

