package com.reviewapp.controller;

import com.reviewapp.model.Staff;
import com.reviewapp.model.User;
import com.reviewapp.service.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * StaffController – Component 04: Staff / Photographer Management
 */
@Controller
public class StaffController {

    @Autowired
    private StaffService staffService;

    // ── Public staff list ────────────────────────────────────────────────────

    @GetMapping("/staff")
    public String staffList(
            @RequestParam(required = false) String role,
            Model model) {
        java.util.List<Staff> staff = (role != null && !role.trim().isEmpty())
                ? staffService.getByRole(role)
                : staffService.getAllStaff();
        model.addAttribute("staffList", staff);
        model.addAttribute("filterRole", role);
        model.addAttribute("pageTitle", "Our Team");
        return "staff_list";
    }

    // ── Admin: add staff ─────────────────────────────────────────────────────

    @GetMapping("/admin/staff/add")
    public String showAddStaff(HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) { ra.addFlashAttribute("errorMsg", "Admin access required."); return "redirect:/login"; }
        return "admin_add_staff";
    }

    @PostMapping("/admin/staff/add")
    public String handleAddStaff(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "") String phone,
            @RequestParam String role,
            HttpSession session,
            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            staffService.addStaff(name, email, phone, role);
            ra.addFlashAttribute("successMsg", "Staff member added successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/staff";
    }

    // ── Admin: edit staff ────────────────────────────────────────────────────

    @GetMapping("/admin/staff/edit/{id}")
    public String showEditStaff(@PathVariable String id, HttpSession session,
                                Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) { ra.addFlashAttribute("errorMsg", "Admin access required."); return "redirect:/login"; }
        Staff s = staffService.findById(id);
        if (s == null) { ra.addFlashAttribute("errorMsg", "Staff member not found."); return "redirect:/staff"; }
        model.addAttribute("staffMember", s);
        model.addAttribute("pageTitle", "Edit Staff");
        return "admin_edit_staff";
    }

    @PostMapping("/admin/staff/edit")
    public String handleEditStaff(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "") String phone,
            @RequestParam String role,
            @RequestParam(defaultValue = "false") boolean available,
            HttpSession session,
            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            staffService.updateStaff(id, name, email, phone, role, available);
            ra.addFlashAttribute("successMsg", "Staff member updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/staff";
    }

    // ── Admin: delete staff ──────────────────────────────────────────────────

    @PostMapping("/admin/staff/delete/{id}")
    public String deleteStaff(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            staffService.deleteStaff(id);
            ra.addFlashAttribute("successMsg", "Staff member deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/staff";
    }

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && "ADMIN".equals(u.getRole());
    }
}

