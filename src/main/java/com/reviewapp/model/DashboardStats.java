package com.reviewapp.model;

/**
 * DashboardStats – Component 05: Admin Dashboard
 *
 * OOP – ENCAPSULATION : aggregated summary data, accessed only through getters.
 * OOP – ABSTRACTION   : hides the complexity of reading multiple files;
 *                        the view only sees clean counts and labels.
 */
public class DashboardStats {

    private final int totalUsers;
    private final int totalPackages;
    private final int totalBookings;
    private final int totalStaff;
    private final int totalReviews;
    private final int pendingBookings;
    private final int availablePackages;
    private final int availableStaff;

    public DashboardStats(int totalUsers, int totalPackages, int totalBookings,
                          int totalStaff, int totalReviews,
                          int pendingBookings, int availablePackages, int availableStaff) {
        this.totalUsers        = totalUsers;
        this.totalPackages     = totalPackages;
        this.totalBookings     = totalBookings;
        this.totalStaff        = totalStaff;
        this.totalReviews      = totalReviews;
        this.pendingBookings   = pendingBookings;
        this.availablePackages = availablePackages;
        this.availableStaff    = availableStaff;
    }

    public int getTotalUsers()        { return totalUsers; }
    public int getTotalPackages()     { return totalPackages; }
    public int getTotalBookings()     { return totalBookings; }
    public int getTotalStaff()        { return totalStaff; }
    public int getTotalReviews()      { return totalReviews; }
    public int getPendingBookings()   { return pendingBookings; }
    public int getAvailablePackages() { return availablePackages; }
    public int getAvailableStaff()    { return availableStaff; }
}

