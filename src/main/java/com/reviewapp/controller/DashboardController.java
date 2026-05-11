package com.reviewapp.controller;

import com.reviewapp.model.Announcement;
import com.reviewapp.model.User;
import com.reviewapp.service.AnnouncementService;
import com.reviewapp.service.DashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * DashboardController – Component 05: Dashboard Integration
 *
 * OOP – ABSTRACTION    : dashboard stats hidden behind DashboardService.
 * OOP – ENCAPSULATION  : admin-only routes protected by isAdmin() helper.
 *
 * CRUD for Announcements:
 *   CREATE → GET/POST /admin/announcements/add
 *   READ   → GET /dashboard  +  GET /admin/announcements
 *   UPDATE → GET/POST /admin/announcements/edit/{id}
 *   DELETE → POST /admin/announcements/delete/{id}
 */
@Controller
public class DashboardController {

    @Autowired private DashboardService    dashboardService;
    @Autowired private AnnouncementService announcementService;

    // ── Dashboard home (READ – visible to all) ───────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null && "ADMIN".equals(user.getRole())) {
            model.addAttribute("stats", dashboardService.getStats());
        }
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("user", user);
        return "dashboard";
    }

    // ── Admin: list announcements (READ) ─────────────────────────────────────

    @GetMapping("/admin/announcements")
    public String listAnnouncements(HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("errorMsg", "Admin access required.");
            return "redirect:/login";
        }
        model.addAttribute("announcements", announcementService.getAllAnnouncements());
        model.addAttribute("pageTitle", "Manage Announcements");
        return "admin_announcements";
    }

    // ── Admin: add announcement (CREATE) ─────────────────────────────────────

    @GetMapping("/admin/announcements/add")
    public String showAddForm(HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) { ra.addFlashAttribute("errorMsg", "Admin access required."); return "redirect:/login"; }
        return "admin_add_announcement";
    }

    @PostMapping("/admin/announcements/add")
    public String handleAdd(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam String priority,
            HttpSession session,
            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            announcementService.addAnnouncement(title, message, priority);
            ra.addFlashAttribute("successMsg", "Announcement added successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/announcements";
    }

    // ── Admin: edit announcement (UPDATE) ────────────────────────────────────

    @GetMapping("/admin/announcements/edit/{id}")
    public String showEditForm(@PathVariable String id, HttpSession session,
                               Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) { ra.addFlashAttribute("errorMsg", "Admin access required."); return "redirect:/login"; }
        Announcement a = announcementService.findById(id);
        if (a == null) { ra.addFlashAttribute("errorMsg", "Announcement not found."); return "redirect:/admin/announcements"; }
        model.addAttribute("announcement", a);
        model.addAttribute("pageTitle", "Edit Announcement");
        return "admin_edit_announcement";
    }

    @PostMapping("/admin/announcements/edit")
    public String handleEdit(
            @RequestParam String id,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam String priority,
            HttpSession session,
            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            announcementService.updateAnnouncement(id, title, message, priority);
            ra.addFlashAttribute("successMsg", "Announcement updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/announcements";
    }

    // ── Admin: delete announcement (DELETE) ──────────────────────────────────

    @PostMapping("/admin/announcements/delete/{id}")
    public String handleDelete(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            announcementService.deleteAnnouncement(id);
            ra.addFlashAttribute("successMsg", "Announcement deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/announcements";
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && "ADMIN".equals(u.getRole());
    }
}
