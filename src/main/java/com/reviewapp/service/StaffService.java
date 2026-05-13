package com.reviewapp.service;

import com.reviewapp.model.Photographer;
import com.reviewapp.model.Staff;
import com.reviewapp.model.Videographer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StaffService – Component 04: Staff / Photographer Management
 * File format: id|name|email|phone|role|available
 */
@Service
public class StaffService {

    private static final String SEP = "|";

    @Value("${staff.file.path}")
    private String filePath;

    // ── READ ────────────────────────────────────────────────────────────────

    public List<Staff> getAllStaff() {
        try {
            File f = new File(filePath);
            if (!f.exists()) return new ArrayList<>();
            List<Staff> list = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get(filePath))) {
                if (line.trim().isEmpty()) continue;
                Staff s = parseLine(line);
                if (s != null) list.add(s);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Error reading staff file", e);
        }
    }

    public List<Staff> getByRole(String role) {
        return getAllStaff().stream()
                .filter(s -> s.getStaffRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    public Staff findById(String id) {
        return getAllStaff().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst().orElse(null);
    }

    // ── CREATE ──────────────────────────────────────────────────────────────

    public void addStaff(String name, String email, String phone, String role) {
        validateStaffInput(name, email, phone, role);
        String id = UUID.randomUUID().toString();
        Staff staff = buildStaff(id, name, email, phone, role, true);
        appendStaff(staff);
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────

    public void updateStaff(String id, String name, String email,
                            String phone, String role, boolean available) {
        validateStaffInput(name, email, phone, role);
        List<Staff> list = getAllStaff();
        list.removeIf(s -> s.getId().equals(id));
        list.add(buildStaff(id, name, email, phone, role, available));
        saveAll(list);
    }

    // ── DELETE ──────────────────────────────────────────────────────────────

    public void deleteStaff(String id) {
        List<Staff> list = getAllStaff();
        list.removeIf(s -> s.getId().equals(id));
        saveAll(list);
    }

    // ── Validation ──────────────────────────────────────────────────────────

    private void validateStaffInput(String name, String email, String phone, String role) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty())
            errors.add("Staff name is required.");
        else if (!name.trim().matches("[A-Za-z .]{2,80}"))
            errors.add("Name may only contain letters, spaces and dots (2-80 chars).");
        if (email == null || email.trim().isEmpty())
            errors.add("Email is required.");
        else if (!email.trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            errors.add("Invalid email address.");
        if (phone != null && !phone.trim().isEmpty()
                && !phone.trim().matches("[0-9+\\-() ]{7,15}"))
            errors.add("Invalid phone number.");
        if (role == null || (!role.equalsIgnoreCase("PHOTOGRAPHER")
                           && !role.equalsIgnoreCase("VIDEOGRAPHER")))
            errors.add("Role must be PHOTOGRAPHER or VIDEOGRAPHER.");
        if (!errors.isEmpty())
            throw new IllegalArgumentException(String.join(" ", errors));
    }

    // ── File I/O ────────────────────────────────────────────────────────────

    private Staff buildStaff(String id, String name, String email,
                              String phone, String role, boolean available) {
        if ("VIDEOGRAPHER".equalsIgnoreCase(role))
            return new Videographer(id, name, email, phone, available);
        return new Photographer(id, name, email, phone, available);
    }

    private void appendStaff(Staff s) {
        try {
            String line = formatLine(s) + System.lineSeparator();
            Files.write(Paths.get(filePath), line.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error saving staff", e);
        }
    }

    private void saveAll(List<Staff> list) {
        try {
            List<String> lines = list.stream().map(this::formatLine).collect(Collectors.toList());
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException("Error saving staff", e);
        }
    }

    private String formatLine(Staff s) {
        return String.join(SEP,
                s.getId(),
                sanitise(s.getName()),
                sanitise(s.getEmail()),
                sanitise(safe(s.getPhone())),
                s.getStaffRole(),
                String.valueOf(s.isAvailable()));
    }

    private String sanitise(String s) {
        return s == null ? "" : s.replace("|", "").replace("\n", "").replace("\r", "");
    }

    private Staff parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 6) return null;
            String id = p[0], name = p[1], email = p[2],
                    phone = p[3], role = p[4];
            boolean avail = Boolean.parseBoolean(p[5]);
            return buildStaff(id, name, email, phone, role, avail);
        } catch (Exception e) { return null; }
    }

    private String safe(String s) { return s == null ? "" : s; }
}

