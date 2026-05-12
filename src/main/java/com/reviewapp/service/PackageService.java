package com.reviewapp.service;

import com.reviewapp.model.Package;
import com.reviewapp.model.PhotoPackage;
import com.reviewapp.model.VideoPackage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PackageService – Component 02: Package Management
 * File format: id|name|type|description|price|durationHours|available
 */
@Service
public class PackageService {

    private static final String SEP = "|";

    @Value("${packages.file.path}")
    private String filePath;

    // ── READ ────────────────────────────────────────────────────────────────

    public List<Package> getAllPackages() {
        try {
            File f = new File(filePath);
            if (!f.exists()) return new ArrayList<>();
            List<Package> list = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get(filePath))) {
                if (line.trim().isEmpty()) continue;
                Package p = parseLine(line);
                if (p != null) list.add(p);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Error reading packages file", e);
        }
    }

    public List<Package> getAvailablePackages() {
        return getAllPackages().stream().filter(Package::isAvailable).collect(Collectors.toList());
    }

    public Package findById(String id) {
        return getAllPackages().stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Package> search(String query) {
        String q = query.toLowerCase();
        return getAllPackages().stream()
                .filter(p -> p.getName().toLowerCase().contains(q)
                          || p.getPackageType().toLowerCase().contains(q)
                          || p.getDescription().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    // ── CREATE ──────────────────────────────────────────────────────────────

    public void addPackage(String name, String type, String description,
                           double price, int durationHours) {
        validatePackageInput(name, type, description, price, durationHours);
        String id = UUID.randomUUID().toString();
        Package pkg = buildPackage(id, name.trim(), type.trim(),
                description.trim(), price, durationHours, true);
        appendPackage(pkg);
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────

    public void updatePackage(String id, String name, String type, String description,
                              double price, int durationHours, boolean available) {
        validatePackageInput(name, type, description, price, durationHours);
        List<Package> list = getAllPackages();
        list.removeIf(p -> p.getId().equals(id));
        list.add(buildPackage(id, name.trim(), type.trim(),
                description.trim(), price, durationHours, available));
        saveAll(list);
    }

    // ── DELETE ──────────────────────────────────────────────────────────────

    public void deletePackage(String id) {
        List<Package> list = getAllPackages();
        list.removeIf(p -> p.getId().equals(id));
        saveAll(list);
    }

    // ── Validation ──────────────────────────────────────────────────────────

    private void validatePackageInput(String name, String type, String description,
                                      double price, int durationHours) {
        List<String> errors = new ArrayList<>();
        if (name == null || name.trim().isEmpty())
            errors.add("Package name is required.");
        else if (name.trim().length() > 100)
            errors.add("Package name must not exceed 100 characters.");

        if (type == null || (!type.equalsIgnoreCase("PHOTO") && !type.equalsIgnoreCase("VIDEO")))
            errors.add("Package type must be PHOTO or VIDEO.");

        if (description == null || description.trim().isEmpty())
            errors.add("Description is required.");
        else if (description.trim().length() < 10)
            errors.add("Description must be at least 10 characters.");
        else if (description.trim().length() > 500)
            errors.add("Description must not exceed 500 characters.");

        if (price <= 0)
            errors.add("Price must be greater than zero.");
        else if (price > 10_000_000)
            errors.add("Price seems unrealistically high. Please check.");

        if (durationHours <= 0 || durationHours > 72)
            errors.add("Duration must be between 1 and 72 hours.");

        if (!errors.isEmpty())
            throw new IllegalArgumentException(String.join(" ", errors));
    }

    // ── File I/O ────────────────────────────────────────────────────────────

    private Package buildPackage(String id, String name, String type, String description,
                                 double price, int durationHours, boolean available) {
        if ("VIDEO".equalsIgnoreCase(type))
            return new VideoPackage(id, name, description, price, durationHours, available);
        return new PhotoPackage(id, name, description, price, durationHours, available);
    }

    private void appendPackage(Package pkg) {
        try {
            String line = formatLine(pkg) + System.lineSeparator();
            Files.write(Paths.get(filePath), line.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error saving package", e);
        }
    }

    private void saveAll(List<Package> list) {
        try {
            List<String> lines = list.stream().map(this::formatLine).collect(Collectors.toList());
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException("Error saving packages", e);
        }
    }

    private String formatLine(Package p) {
        return String.join(SEP,
                p.getId(), p.getName(), p.getPackageType(),
                p.getDescription().replace("|", ";"),
                String.valueOf(p.getPrice()),
                String.valueOf(p.getDurationHours()),
                String.valueOf(p.isAvailable()));
    }

    private Package parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 7) return null;
            String id = p[0], name = p[1], type = p[2], desc = p[3];
            double price = Double.parseDouble(p[4]);
            int dur      = Integer.parseInt(p[5]);
            boolean avail = Boolean.parseBoolean(p[6]);
            return buildPackage(id, name, type, desc, price, dur, avail);
        } catch (Exception e) { return null; }
    }
}

