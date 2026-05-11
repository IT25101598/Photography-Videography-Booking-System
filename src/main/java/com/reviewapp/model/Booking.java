package com.reviewapp.model;

/**
 * Booking.java – Base class (Component 03: Booking Management)
 *
 * OOP – ENCAPSULATION : private fields + getters/setters
 * OOP – ABSTRACTION   : abstract class; subclasses define booking rules
 * OOP – INHERITANCE   : WeddingBooking and BirthdayBooking extend this
 */
public abstract class Booking {

    private String id;
    private String customerId;
    private String customerName;
    private String packageId;
    private String packageName;
    private String eventDate;   // yyyy-MM-dd
    private String status;      // PENDING | CONFIRMED | CANCELLED
    private String notes;

    public Booking() {}

    public Booking(String id, String customerId, String customerName,
                   String packageId, String packageName,
                   String eventDate, String status, String notes) {
        this.id           = id;
        this.customerId   = customerId;
        this.customerName = customerName;
        this.packageId    = packageId;
        this.packageName  = packageName;
        this.eventDate    = eventDate;
        this.status       = status;
        this.notes        = notes;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public String getId()                        { return id; }
    public void   setId(String id)               { this.id = id; }

    public String getCustomerId()                { return customerId; }
    public void   setCustomerId(String c)        { this.customerId = c; }

    public String getCustomerName()              { return customerName; }
    public void   setCustomerName(String n)      { this.customerName = n; }

    public String getPackageId()                 { return packageId; }
    public void   setPackageId(String p)         { this.packageId = p; }

    public String getPackageName()               { return packageName; }
    public void   setPackageName(String p)       { this.packageName = p; }

    public String getEventDate()                 { return eventDate; }
    public void   setEventDate(String d)         { this.eventDate = d; }

    public String getStatus()                    { return status; }
    public void   setStatus(String s)            { this.status = s; }

    public String getNotes()                     { return notes; }
    public void   setNotes(String n)             { this.notes = n; }

    /** OOP – POLYMORPHISM : return event type string */
    public abstract String getEventType();

    /** OOP – POLYMORPHISM : special booking rule description */
    public abstract String getBookingRule();

    @Override
    public String toString() {
        return "Booking{id='" + id + "', customer='" + customerName
                + "', event='" + getEventType() + "', date='" + eventDate + "'}";
    }
}

