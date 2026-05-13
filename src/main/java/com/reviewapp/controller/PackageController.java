package com.reviewapp.controller;

import com.reviewapp.model.Package;
import com.reviewapp.model.User;
import com.reviewapp.service.PackageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * PackageController – Component 02: Package Management
 */
@Controller
public class PackageController {

    @Autowired
    private PackageService packageService;

    // ── Customer: package listing ────────────────────────────────────────────

    @GetMapping("/packages")
    public String listPackages(
            @RequestParam(required = false) String query,
            Model model) {
        java.util.List<Package> packages = (query != null && !query.trim().isEmpty())
                ? packageService.search(query)
                : packageService.getAvailablePackages();
        model.addAttribute("packages", packages);
        model.addAttribute("query", query);
        model.addAttribute("pageTitle", "Our Packages");
        return "packages";
    }

    // ── Admin: package list ──────────────────────────────────────────────────

    @GetMapping("/admin/packages")
    public String adminPackageList(HttpSession session, Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) {
            ra.addFlashAttribute("errorMsg", "Admin access required.");
            return "redirect:/login";
        }
        model.addAttribute("packages", packageService.getAllPackages());
        model.addAttribute("pageTitle", "Manage Packages");
        return "admin_packages";
    }

    // ── Admin: add package ───────────────────────────────────────────────────

    @GetMapping("/admin/packages/add")
    public String showAddPackage(HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) { ra.addFlashAttribute("errorMsg", "Admin access required."); return "redirect:/login"; }
        return "admin_add_package";
    }

    @PostMapping("/admin/packages/add")
    public String handleAddPackage(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam int durationHours,
            HttpSession session,
            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            packageService.addPackage(name, type, description, price, durationHours);
            ra.addFlashAttribute("successMsg", "Package added successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    // ── Admin: edit package ──────────────────────────────────────────────────

    @GetMapping("/admin/packages/edit/{id}")
    public String showEditPackage(@PathVariable String id, HttpSession session,
                                  Model model, RedirectAttributes ra) {
        if (!isAdmin(session)) { ra.addFlashAttribute("errorMsg", "Admin access required."); return "redirect:/login"; }
        Package pkg = packageService.findById(id);
        if (pkg == null) { ra.addFlashAttribute("errorMsg", "Package not found."); return "redirect:/admin/packages"; }
        model.addAttribute("pkg", pkg);
        model.addAttribute("pageTitle", "Edit Package");
        return "admin_edit_package";
    }

    @PostMapping("/admin/packages/edit")
    public String handleEditPackage(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam int durationHours,
            @RequestParam(defaultValue = "false") boolean available,
            HttpSession session,
            RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            packageService.updatePackage(id, name, type, description, price, durationHours, available);
            ra.addFlashAttribute("successMsg", "Package updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    // ── Admin: delete package ────────────────────────────────────────────────

    @PostMapping("/admin/packages/delete/{id}")
    public String deletePackage(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            packageService.deletePackage(id);
            ra.addFlashAttribute("successMsg", "Package deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    private boolean isAdmin(HttpSession session) {
        User u = (User) session.getAttribute("loggedInUser");
        return u != null && "ADMIN".equals(u.getRole());
    }
}

