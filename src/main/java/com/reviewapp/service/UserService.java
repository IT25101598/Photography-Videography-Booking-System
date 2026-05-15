package com.reviewapp.service;

import com.reviewapp.model.AdminUser;
import com.reviewapp.model.CustomerUser;
import com.reviewapp.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UserService – Component 01: User Management
 * Handles CRUD for users persisted to a flat file.
 *
 * File format: id|username|password|email|phone|role
 */
@Service
public class UserService {

    private static final String SEP = "|";

    @Value("${users.file.path}")
    private String filePath;

    // ── READ ────────────────────────────────────────────────────────────────

    public List<User> getAllUsers() {
        try {
            File f = new File(filePath);
            if (!f.exists()) return new ArrayList<>();
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<User> users = new ArrayList<>();
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                User u = parseLine(line);
                if (u != null) users.add(u);
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException("Error reading users file", e);
        }
    }

    public User findByUsername(String username) {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst().orElse(null);
    }

    public User findById(String id) {
        return getAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst().orElse(null);
    }

    public User authenticate(String username, String password) {
        User u = findByUsername(username);
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    // ── CREATE ──────────────────────────────────────────────────────────────

    public void register(String username, String password, String email,
                         String phone, String role) {
        validateUserInput(username, password, email, phone);
        if (findByUsername(username) != null) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }
        String id = UUID.randomUUID().toString();
        User user = "ADMIN".equalsIgnoreCase(role)
                ? new AdminUser(id, username, password, email, phone)
                : new CustomerUser(id, username, password, email, phone);
        appendUser(user);
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────

    public void updateUser(String id, String email, String password, String phone) {
        // Validate fields that are being updated
        List<String> errors = new ArrayList<>();
        if (email == null || email.trim().isEmpty())
            errors.add("Email is required.");
        else if (!email.trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            errors.add("Invalid email address.");
        if (password != null && !password.trim().isEmpty() && password.trim().length() < 6)
            errors.add("New password must be at least 6 characters.");
        if (phone != null && !phone.trim().isEmpty()
                && !phone.trim().matches("[0-9+\\-() ]{7,15}"))
            errors.add("Invalid phone number.");
        if (!errors.isEmpty())
            throw new IllegalArgumentException(String.join(" ", errors));

        List<User> users = getAllUsers();
        boolean found = false;
        for (User u : users) {
            if (u.getId().equals(id)) {
                u.setEmail(email.trim());
                if (password != null && !password.trim().isEmpty()) u.setPassword(password.trim());
                if (phone    != null && !phone.trim().isEmpty())    u.setPhone(phone.trim());
                found = true;
                break;
            }
        }
        if (!found) throw new IllegalArgumentException("User not found.");
        saveAllUsers(users);
    }

    // ── DELETE ──────────────────────────────────────────────────────────────

    public void deleteUser(String id) {
        List<User> users = getAllUsers();
        users.removeIf(u -> u.getId().equals(id));
        saveAllUsers(users);
    }

    // ── Validation ──────────────────────────────────────────────────────────

    private void validateUserInput(String username, String password,
                                   String email, String phone) {
        List<String> errors = new ArrayList<>();

        if (username == null || username.trim().isEmpty())
            errors.add("Username is required.");
        else if (!username.trim().matches("[A-Za-z0-9_]{3,30}"))
            errors.add("Username must be 3-30 characters: letters, digits, underscore only.");

        if (password == null || password.trim().isEmpty())
            errors.add("Password is required.");
        else if (password.trim().length() < 6)
            errors.add("Password must be at least 6 characters.");

        if (email == null || email.trim().isEmpty())
            errors.add("Email is required.");
        else if (!email.trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            errors.add("Invalid email address.");

        if (phone != null && !phone.trim().isEmpty()
                && !phone.trim().matches("[0-9+\\-() ]{7,15}"))
            errors.add("Invalid phone number.");

        if (!errors.isEmpty())
            throw new IllegalArgumentException(String.join(" ", errors));
    }

    // ── File I/O ────────────────────────────────────────────────────────────

    private void appendUser(User user) {
        try {
            String line = formatLine(user) + System.lineSeparator();
            Files.write(Paths.get(filePath), line.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }

    private void saveAllUsers(List<User> users) {
        try {
            List<String> lines = users.stream().map(this::formatLine).collect(Collectors.toList());
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException("Error saving users", e);
        }
    }

    private String formatLine(User user) {
        return String.join(SEP,
                user.getId(),
                sanitise(user.getUsername()),
                sanitise(user.getPassword()),
                sanitise(user.getEmail()),
                sanitise(safe(user.getPhone())),
                user.getRole());
    }

    private String sanitise(String s) {
        return s == null ? "" : s.replace("|", "").replace("\n", "").replace("\r", "");
    }

    private User parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 6) return null;
            String id = p[0], username = p[1], password = p[2],
                    email = p[3], phone = p[4], role = p[5];
            if ("ADMIN".equalsIgnoreCase(role))
                return new AdminUser(id, username, password, email, phone);
            else
                return new CustomerUser(id, username, password, email, phone);
        } catch (Exception e) { return null; }
    }

    private String safe(String s) { return s == null ? "" : s; }
}

