package com.reviewapp.controller;

import com.reviewapp.model.User;
import com.reviewapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * UserController – Component 01: User Management
 * Handles registration, login, logout, profile update, and admin user list.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // ── Home redirect ────────────────────────────────────────────────────────

    @GetMapping("/")
    public String home() { return "redirect:/dashboard"; }

    // ── Registration ─────────────────────────────────────────────────────────

    @GetMapping("/register")
    public String showRegister() { return "register"; }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "") String phone,
            RedirectAttributes ra) {
        try {
            userService.register(username, password, email, phone, "CUSTOMER");
            ra.addFlashAttribute("successMsg", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/register";
        }
    }

    // ── Login / Logout ───────────────────────────────────────────────────────

    @GetMapping("/login")
    public String showLogin() { return "login"; }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes ra) {
        User user = userService.authenticate(username, password);
        if (user == null) {
            ra.addFlashAttribute("errorMsg", "Invalid username or password.");
            return "redirect:/login";
        }
        session.setAttribute("loggedInUser", user);
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ── Profile ──────────────────────────────────────────────────────────────

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            ra.addFlashAttribute("errorMsg", "Please log in first.");
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "My Profile");
        return "profile";
    }

    @PostMapping("/profile/update")
    public String handleProfileUpdate(
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "") String password,
            @RequestParam(required = false, defaultValue = "") String phone,
            HttpSession session,
            RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        try {
            userService.updateUser(user.getId(), email, password.isEmpty() ? null : password, phone);
            // Refresh session
            User updated = userService.findById(user.getId());
            session.setAttribute("loggedInUser", updated);
            ra.addFlashAttribute("successMsg", "Profile updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/profile";
    }

    // ── Admin: User List ─────────────────────────────────────────────────────

    @GetMapping("/users")
    public String userList(HttpSession session, Model model, RedirectAttributes ra) {
        User admin = (User) session.getAttribute("loggedInUser");
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            ra.addFlashAttribute("errorMsg", "Admin access required.");
            return "redirect:/login";
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("pageTitle", "User Management");
        return "user_list";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable String id, HttpSession session, RedirectAttributes ra) {
        User admin = (User) session.getAttribute("loggedInUser");
        if (admin == null || !"ADMIN".equals(admin.getRole())) return "redirect:/login";
        try {
            userService.deleteUser(id);
            ra.addFlashAttribute("successMsg", "User deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/users";
    }
}

