package com.reviewapp.model;

/**
 * PhotoPackage – OOP INHERITANCE from Package.
 * Represents a photography-only service package.
 */
public class PhotoPackage extends Package {

    public PhotoPackage() { super(); }

    public PhotoPackage(String id, String name, String description,
                        double price, int durationHours, boolean available) {
        super(id, name, description, price, durationHours, available);
    }

    @Override
    public String getPackageType() { return "PHOTO"; }

    @Override
    public String getPackageDetails() {
        return "Professional Photography – " + getDurationHours() + " hrs | Rs. "
                + String.format("%.2f", getPrice());
    }
}

