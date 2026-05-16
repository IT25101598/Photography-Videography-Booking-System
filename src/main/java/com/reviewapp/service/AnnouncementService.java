package com.reviewapp.service;

import com.reviewapp.model.Announcement;
import com.reviewapp.model.StandardAnnouncement;
import com.reviewapp.model.UrgentAnnouncement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AnnouncementService – Component 05: Dashboard Integration
 *
 * OOP – ENCAPSULATION  : file I/O hidden behind clean public methods.
 * OOP – ABSTRACTION    : callers never deal with raw file lines.
 *
 * File format: id|title|message|priority|createdDate
 */
@Service
public class AnnouncementService {

    private static final String SEP = "|";

    @Value("${announcements.file.path}")
    private String filePath;

    // ── READ ─────────────────────────────────────────────────────────────────

    public List<Announcement> getAllAnnouncements() {
        try {
            File f = new File(filePath);
            if (!f.exists()) return new ArrayList<>();
            List<Announcement> list = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get(filePath))) {
                if (line.trim().isEmpty()) continue;
                Announcement a = parseLine(line);
                if (a != null) list.add(a);
            }
            // Sort by priority: HIGH first, then MEDIUM, then LOW
            list.sort(Comparator.comparingInt(a -> priorityOrder(a.getPriority())));
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Error reading announcements file", e);
        }
    }

    public Announcement findById(String id) {
        return getAllAnnouncements().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    public void addAnnouncement(String title, String message, String priority) {
        validateInput(title, message, priority);
        String id = UUID.randomUUID().toString();
        String p = priority.toUpperCase();
        Announcement a;
        if ("HIGH".equals(p)) {
            a = new UrgentAnnouncement(id, sanitise(title), sanitise(message), p, LocalDate.now().toString());
        } else {
            a = new StandardAnnouncement(id, sanitise(title), sanitise(message), p, LocalDate.now().toString());
        }
        appendToFile(a);
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    public void updateAnnouncement(String id, String title, String message, String priority) {
        validateInput(title, message, priority);
        List<Announcement> list = getAllAnnouncements();
        list.removeIf(a -> a.getId().equals(id));
        String p = priority.toUpperCase();
        if ("HIGH".equals(p)) {
            list.add(new UrgentAnnouncement(id, sanitise(title), sanitise(message), p, LocalDate.now().toString()));
        } else {
            list.add(new StandardAnnouncement(id, sanitise(title), sanitise(message), p, LocalDate.now().toString()));
        }
        saveAll(list);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    public void deleteAnnouncement(String id) {
        List<Announcement> list = getAllAnnouncements();
        list.removeIf(a -> a.getId().equals(id));
        saveAll(list);
    }

    // ── Validation ───────────────────────────────────────────────────────────

    private void validateInput(String title, String message, String priority) {
        List<String> errors = new ArrayList<>();
        if (title == null || title.trim().isEmpty())
            errors.add("Title is required.");
        else if (title.trim().length() > 100)
            errors.add("Title must be 100 characters or fewer.");
        if (message == null || message.trim().isEmpty())
            errors.add("Message is required.");
        else if (message.trim().length() > 500)
            errors.add("Message must be 500 characters or fewer.");
        if (priority == null || (!priority.equalsIgnoreCase("HIGH")
                && !priority.equalsIgnoreCase("MEDIUM")
                && !priority.equalsIgnoreCase("LOW")))
            errors.add("Priority must be HIGH, MEDIUM, or LOW.");
        if (!errors.isEmpty())
            throw new IllegalArgumentException(String.join(" ", errors));
    }

    // ── File I/O ─────────────────────────────────────────────────────────────

    private void appendToFile(Announcement a) {
        try {
            String line = formatLine(a) + System.lineSeparator();
            Files.write(Paths.get(filePath), line.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error saving announcement", e);
        }
    }

    private void saveAll(List<Announcement> list) {
        try {
            List<String> lines = list.stream()
                    .map(this::formatLine)
                    .collect(Collectors.toList());
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException("Error saving announcements", e);
        }
    }

    private String formatLine(Announcement a) {
        return String.join(SEP,
                a.getId(),
                sanitise(a.getTitle()),
                sanitise(a.getMessage()),
                a.getPriority(),
                a.getCreatedDate());
    }

    private Announcement parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 5) return null;
            if ("HIGH".equalsIgnoreCase(p[3])) {
                return new UrgentAnnouncement(p[0], p[1], p[2], p[3], p[4]);
            } else {
                return new StandardAnnouncement(p[0], p[1], p[2], p[3], p[4]);
            }
        } catch (Exception e) { return null; }
    }

    private String sanitise(String s) {
        return s == null ? "" : s.replace("|", "").replace("\n", "").replace("\r", "");
    }

    private int priorityOrder(String priority) {
        if ("HIGH".equalsIgnoreCase(priority))   return 0;
        if ("MEDIUM".equalsIgnoreCase(priority)) return 1;
        return 2;
    }
}
