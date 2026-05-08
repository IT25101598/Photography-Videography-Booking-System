package com.reviewapp.service;

import com.reviewapp.model.Booking;
import com.reviewapp.model.DashboardStats;
import com.reviewapp.model.Package;
import com.reviewapp.model.Staff;
import com.reviewapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DashboardService – Component 05: Admin Dashboard
 *
 * OOP – ABSTRACTION : hides multi-file reads behind clean summary methods.
 * OOP – ENCAPSULATION : stats are bundled in DashboardStats.
 */
@Service
public class DashboardService {

    @Autowired private UserService    userService;
    @Autowired private PackageService packageService;
    @Autowired private BookingService bookingService;
    @Autowired private StaffService   staffService;
    @Autowired private ReviewService  reviewService;

    public DashboardStats getStats() {
        List<User>    users    = userService.getAllUsers();
        List<Package> packages = packageService.getAllPackages();
        List<Booking> bookings = bookingService.getAllBookings();
        List<Staff>   staff    = staffService.getAllStaff();

        int totalReviews      = reviewService.getAllReviews().size();
        int pendingBookings   = (int) bookings.stream()
                                    .filter(b -> "PENDING".equalsIgnoreCase(b.getStatus())).count();
        int availablePackages = (int) packages.stream().filter(Package::isAvailable).count();
        int availableStaff    = (int) staff.stream().filter(Staff::isAvailable).count();

        return new DashboardStats(
                users.size(), packages.size(), bookings.size(), staff.size(),
                totalReviews, pendingBookings, availablePackages, availableStaff);
    }
}

