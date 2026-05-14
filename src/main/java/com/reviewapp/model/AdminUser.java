package com.reviewapp.model;

/**
 * AdminUser – OOP INHERITANCE from User.
 * Has elevated privileges: manage packages, bookings, staff, and moderate reviews.
 */
public class AdminUser extends User {

    public AdminUser() { super(); }

    public AdminUser(String id, String username, String password,
                     String email, String phone) {
        super(id, username, password, email, phone);
    }

    /** OOP – POLYMORPHISM : runtime role identification */
    @Override
    public String getRole() { return "ADMIN"; }
}

