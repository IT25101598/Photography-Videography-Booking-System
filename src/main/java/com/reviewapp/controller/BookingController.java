package com.reviewapp.controller;

import com.reviewapp.model.Booking;
import com.reviewapp.model.Package;
import com.reviewapp.model.User;
import com.reviewapp.service.BookingService;
import com.reviewapp.service.PackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * BookingController – Component 03: Booking Management
 */
@Controller
public class BookingController {

    @Autowired private BookingService bookingService;
    @Autowired private PackageService packageService;

    // ── Customer: booking form ───────────────────────────────────────────────

    @GetMapping("/bookings/new")
    public String showBookingForm(
            @RequestParam(required = false) String packageId,
            HttpSession session, Model model, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            ra.addFlashAttribute("errorMsg", "Please log in to book a package.");
            return "redirect:/login";
        }
        model.addAttribute("packages", packageService.getAvailablePackages());
        model.addAttribute("selectedPackageId", packageId);
        model.addAttribute("pageTitle", "Book a Package");
        return "booking_form";
    }

    @PostMapping("/bookings/new")
    public String handleBooking(
            @RequestParam String packageId,
            @RequestParam String eventDate,
            @RequestParam String eventType,
            @RequestParam(required = false, defaultValue = "") String notes,
            HttpSession session,
            RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        try {
            Package pkg = packageService.findById(packageId);
            String packageName = pkg != null ? pkg.getName() : packageId;
            bookingService.addBooking(user.getId(), user.getUsername(),
                    packageId, packageName, eventDate, eventType, notes);
            ra.addFlashAttribute("successMsg", "Booking submitted successfully! Status: PENDING.");
            return "redirect:/bookings/my";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/bookings/new";
        }
    }

    // ── Customer: my bookings ────────────────────────────────────────────────

    @GetMapping("/bookings/my")
    public String myBookings(HttpSession session, Model model, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) { ra.addFlashAttribute("errorMsg", "Please log in."); return "redirect:/login"; }
        model.addAttribute("bookings", bookingService.getBookingsByCustomerId(user.getId()));
        model.addAttribute("pageTitle", "My Bookings");
        return "my_bookings";
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        try {
            Booking b = bookingService.findById(id);
            if (b != null && b.getCustomerId().equals(user.getId())) {
                bookingService.updateStatus(id, "CANCELLED");
                ra.addFlashAttribute("successMsg", "Booking cancelled.");
            } else {
                ra.addFlashAttribute("errorMsg", "Booking not found or access denied.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/bookings/my";
    }

    // ── Admin: all bookings ──────────────────────────────────────────────────

    @GetMapping("/admin/bookings")
    public String adminBookings(HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) { ra.addFlashAttribute("errorMsg", "Admin access required."); return "redirect:/login"; }
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("packages", packageService.getAllPackages());
        model.addAttribute("pageTitle", "Manage Bookings");
        return "admin_bookings";
    }

    @PostMapping("/admin/bookings/status")
    public String updateStatus(
            @RequestParam String id,
            @RequestParam String status,
            HttpSession session,
            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            bookingService.updateStatus(id, status);
            ra.addFlashAttribute("successMsg", "Booking status updated to " + status + ".");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/delete/{id}")
    public String deleteBooking(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            bookingService.deleteBooking(id);
            ra.addFlashAttribute("successMsg", "Booking deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && "ADMIN".equals(u.getRole());
    }
}

