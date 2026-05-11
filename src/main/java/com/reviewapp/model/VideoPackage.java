package com.reviewapp.model;

/**
 * VideoPackage – OOP INHERITANCE from Package.
 * Represents a videography-only service package.
 * OOP – POLYMORPHISM: overrides calculateFinalPrice() to add editing surcharge.
 */
public class VideoPackage extends Package {

    private static final double EDITING_SURCHARGE = 5000.0;

    public VideoPackage() { super(); }

    public VideoPackage(String id, String name, String description,
                        double price, int durationHours, boolean available) {
        super(id, name, description, price, durationHours, available);
    }

    @Override
    public String getPackageType() { return "VIDEO"; }

    /** Includes post-production editing surcharge */
    @Override
    public double calculateFinalPrice() {
        return getPrice() + EDITING_SURCHARGE;
    }

    @Override
    public String getPackageDetails() {
        return "Professional Videography – " + getDurationHours() + " hrs | Rs. "
                + String.format("%.2f", calculateFinalPrice())
                + " (incl. editing)";
    }
}

