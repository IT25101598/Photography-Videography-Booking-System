package com.reviewapp.model;

/** WeddingBooking – OOP INHERITANCE from Booking. */
public class WeddingBooking extends Booking {

    public WeddingBooking() { super(); }

    public WeddingBooking(String id, String customerId, String customerName,
                          String packageId, String packageName,
                          String eventDate, String status, String notes) {
        super(id, customerId, customerName, packageId, packageName, eventDate, status, notes);
    }

    @Override
    public String getEventType() { return "WEDDING"; }

    @Override
    public String getBookingRule() {
        return "Wedding bookings must be made at least 30 days in advance.";
    }
}

