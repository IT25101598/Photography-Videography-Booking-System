package com.reviewapp.controller;

import com.reviewapp.model.Booking;
import com.reviewapp.model.Review;
import com.reviewapp.model.User;
import com.reviewapp.service.BookingService;
import com.reviewapp.service.PackageService;
import com.reviewapp.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReviewController {

    @Autowired private ReviewService  reviewService;
    @Autowired private BookingService bookingService;
    @Autowired private PackageService packageService;

    // ── User: Submit Review ──────────────────────────────────────────────────

    @GetMapping("/submitReview")
    public String showSubmitForm(HttpSession session, Model model, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            ra.addFlashAttribute("errorMsg", "Please log in to submit a review.");
            return "redirect:/login";
        }

        // Fetch only CONFIRMED bookings for this user
        List<Booking> confirmedBookings = bookingService
                .getBookingsByCustomerId(user.getId())
                .stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                .collect(Collectors.toList());

        model.addAttribute("confirmedBookings", confirmedBookings);
        model.addAttribute("pageTitle", "Submit Review");
        return "submit_review";
    }

    @PostMapping("/submitReview")
    public String handleSubmit(
            @RequestParam String packageId,
            @RequestParam String authorName,
            @RequestParam String content,
            @RequestParam int rating,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        try {
            // Validate that this packageId belongs to a confirmed booking of this user
            boolean owns = bookingService.getBookingsByCustomerId(user.getId())
                    .stream()
                    .anyMatch(b -> b.getPackageId().equals(packageId)
                               && "CONFIRMED".equalsIgnoreCase(b.getStatus()));
            if (!owns) throw new IllegalArgumentException(
                    "You can only review packages from your confirmed bookings.");

            reviewService.addReview(packageId, authorName, content, rating);
            redirectAttributes.addFlashAttribute("successMsg", "Review submitted successfully! Thank you.");
            return "redirect:/submitReview";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
            return "redirect:/submitReview";
        }
    }

    // ── User: Edit Own Review ────────────────────────────────────────────────

    @PostMapping("/review/edit/{id}")
    public String handleUserEdit(
            @PathVariable String id,
            @RequestParam String content,
            @RequestParam int rating,
            HttpSession session,
            RedirectAttributes ra) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            ra.addFlashAttribute("errorMsg", "Please log in first.");
            return "redirect:/login";
        }

        Review review = reviewService.getReviewById(id);
        if (review == null) {
            ra.addFlashAttribute("errorMsg", "Review not found.");
            return "redirect:/viewReviews";
        }

        // Only allow the owner (matched by username) to edit
        if (!review.getAuthorName().equalsIgnoreCase(user.getUsername())) {
            ra.addFlashAttribute("errorMsg", "You can only edit your own reviews.");
            return "redirect:/viewReviews";
        }

        try {
            reviewService.updateReview(id, review.getPackageId(), review.getAuthorName(),
                    content, rating, review.getReviewType());
            ra.addFlashAttribute("successMsg", "Your review has been updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error updating review: " + e.getMessage());
        }
        return "redirect:/viewReviews";
    }

    // ── User: Delete Own Review ──────────────────────────────────────────────

    @PostMapping("/review/delete/{id}")
    public String handleUserDelete(
            @PathVariable String id,
            HttpSession session,
            RedirectAttributes ra) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            ra.addFlashAttribute("errorMsg", "Please log in first.");
            return "redirect:/login";
        }

        Review review = reviewService.getReviewById(id);
        if (review == null) {
            ra.addFlashAttribute("errorMsg", "Review not found.");
            return "redirect:/viewReviews";
        }

        // Only allow the owner (matched by username) to delete
        if (!review.getAuthorName().equalsIgnoreCase(user.getUsername())) {
            ra.addFlashAttribute("errorMsg", "You can only delete your own reviews.");
            return "redirect:/viewReviews";
        }

        try {
            reviewService.deleteReview(id);
            ra.addFlashAttribute("successMsg", "Your review has been deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error deleting review: " + e.getMessage());
        }
        return "redirect:/viewReviews";
    }

    // ── Public: View All Reviews ─────────────────────────────────────────────

    @GetMapping("/viewReviews")
    public String viewReviews(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "0") int minRating,
            @RequestParam(required = false) String reviewType,
            Model model) {
        String trimmed = (query != null) ? query.trim() : "";
        // sanitise
        trimmed = trimmed.replaceAll("[|<>]", "").substring(0, Math.min(trimmed.length(), 100));
        List<Review> reviews = reviewService.search(trimmed, minRating,
                (reviewType != null && !reviewType.isEmpty()) ? reviewType : null);
        model.addAttribute("reviews", reviews);
        model.addAttribute("searchQuery", trimmed);
        model.addAttribute("minRating", minRating);
        model.addAttribute("reviewType", reviewType != null ? reviewType : "");
        model.addAttribute("pageTitle", "View Reviews");
        return "view_reviews";
    }

    // ── Admin: View All Reviews (no edit – delete only) ──────────────────────

    @GetMapping("/adminModeration")
    public String adminPanel(HttpSession session, Model model, RedirectAttributes ra) {
        User admin = (User) session.getAttribute("loggedInUser");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            ra.addFlashAttribute("errorMsg", "Admin access required.");
            return "redirect:/login";
        }
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("pageTitle", "Admin Panel");
        return "admin_moderation";
    }

    // ── Admin: Delete Any Review ─────────────────────────────────────────────

    @PostMapping("/adminModeration/delete/{id}")
    public String handleAdminDelete(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        User admin = (User) session.getAttribute("loggedInUser");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            redirectAttributes.addFlashAttribute("errorMsg", "Admin access required.");
            return "redirect:/login";
        }
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("successMsg", "Review deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Error deleting: " + e.getMessage());
        }
        return "redirect:/adminModeration";
    }
}
