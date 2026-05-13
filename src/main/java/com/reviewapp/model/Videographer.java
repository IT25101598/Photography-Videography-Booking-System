package com.reviewapp.model;

/** Videographer – OOP INHERITANCE from Staff. */
public class Videographer extends Staff {

    public Videographer() { super(); }

    public Videographer(String id, String name, String email,
                        String phone, boolean available) {
        super(id, name, email, phone, available);
    }

    @Override
    public String getStaffRole() { return "VIDEOGRAPHER"; }

    @Override
    public String getServiceDescription() {
        return "Films, edits and produces cinematic video coverage for events and productions.";
    }
}

