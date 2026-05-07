package com.reviewapp.model;

/**
 * CustomerUser – OOP INHERITANCE from User.
 * Represents a regular customer who can browse packages and submit bookings/reviews.
 */
public class CustomerUser extends User {

    public CustomerUser() { super(); }

    public CustomerUser(String id, String username, String password,
                        String email, String phone) {
        super(id, username, password, email, phone);
    }

    /** OOP – POLYMORPHISM : runtime role identification */
    @Override
    public String getRole() { return "CUSTOMER"; }
}

