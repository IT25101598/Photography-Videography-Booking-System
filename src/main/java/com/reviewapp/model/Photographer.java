package com.reviewapp.model;

/** Photographer – OOP INHERITANCE from Staff. */
public class Photographer extends Staff {

    public Photographer() { super(); }

    public Photographer(String id, String name, String email,
                        String phone, boolean available) {
        super(id, name, email, phone, available);
    }

    @Override
    public String getStaffRole() { return "PHOTOGRAPHER"; }

    @Override
    public String getServiceDescription() {
        return "Captures high-quality still images for events, portraits and commercial shoots.";
    }
}

