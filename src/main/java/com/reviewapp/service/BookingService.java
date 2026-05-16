package com.reviewapp.service;

import com.reviewapp.model.Booking;
import com.reviewapp.model.BirthdayBooking;
import com.reviewapp.model.WeddingBooking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BookingService – Component 03: Booking Management
 * File format: id|customerId|customerName|packageId|packageName|eventDate|eventType|status|notes
 */
@Service
public class BookingService {

    private static final String SEP = "|";

    @Value("${bookings.file.path}")
    private String filePath;

    // ── READ ────────────────────────────────────────────────────────────────

    public List<Booking> getAllBookings() {
        try {
            File f = new File(filePath);
            if (!f.exists()) return new ArrayList<>();
            List<Booking> list = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get(filePath))) {
                if (line.trim().isEmpty()) continue;
                Booking b = parseLine(line);
                if (b != null) list.add(b);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Error reading bookings file", e);
        }
    }

    public List<Booking> getBookingsByCustomerId(String customerId) {
        return getAllBookings().stream()
                .filter(b -> b.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public Booking findById(String id) {
        return getAllBookings().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst().orElse(null);
    }

    public boolean isDateAvailable(String eventDate, String packageId) {
        return getAllBookings().stream()
                .noneMatch(b -> b.getPackageId().equals(packageId)
                             && b.getEventDate().equals(eventDate)
                             && !"CANCELLED".equalsIgnoreCase(b.getStatus()));
    }

    // ── CREATE ──────────────────────────────────────────────────────────────

    public void addBooking(String customerId, String customerName,
                           String packageId, String packageName,
                           String eventDate, String eventType, String notes) {
        validateBookingInput(customerName, packageId, eventDate, eventType);
        if (!isDateAvailable(eventDate, packageId)) {
            throw new IllegalArgumentException(
                    "Package '" + packageName + "' is already booked on " + eventDate + ".");
        }
        String id = UUID.randomUUID().toString();
        Booking booking = buildBooking(id, customerId, customerName,
                packageId, packageName, eventDate, eventType, "PENDING", safe(notes));
        appendBooking(booking);
    }

    // ── UPDATE ──────────────────────────────────────────────────────────────

    public void updateBooking(String id, String customerName, String packageId,
                              String packageName, String eventDate,
                              String eventType, String status, String notes) {
        validateBookingInput(customerName, packageId, eventDate, eventType);
        List<Booking> list = getAllBookings();
        // preserve the original customerId before removing
        String customerId = list.stream()
                .filter(b -> b.getId().equals(id))
                .map(Booking::getCustomerId)
                .findFirst().orElse("");
        list.removeIf(b -> b.getId().equals(id));
        list.add(buildBooking(id, customerId, customerName, packageId, packageName,
                eventDate, eventType, status, safe(notes)));
        saveAll(list);
    }

    public void updateStatus(String id, String status) {
        List<Booking> list = getAllBookings();
        list.stream().filter(b -> b.getId().equals(id)).findFirst()
                .ifPresent(b -> b.setStatus(status));
        saveAll(list);
    }

    // ── DELETE ──────────────────────────────────────────────────────────────

    public void deleteBooking(String id) {
        List<Booking> list = getAllBookings();
        list.removeIf(b -> b.getId().equals(id));
        saveAll(list);
    }

    // ── Validation ──────────────────────────────────────────────────────────

    private static final java.util.Set<String> ALLOWED_EVENT_TYPES =
            java.util.Set.of("WEDDING", "BIRTHDAY", "PRESHOOT", "OTHER");

    private void validateBookingInput(String customerName, String packageId,
                                      String eventDate, String eventType) {
        List<String> errors = new ArrayList<>();

        if (customerName == null || customerName.trim().isEmpty())
            errors.add("Customer name is required.");
        else if (customerName.trim().length() > 100)
            errors.add("Customer name must not exceed 100 characters.");
        else if (!customerName.trim().matches("[A-Za-z ]{2,100}"))
            errors.add("Customer name may only contain letters and spaces (2–100 chars).");

        if (packageId == null || packageId.trim().isEmpty())
            errors.add("Package selection is required.");

        if (eventDate == null || eventDate.trim().isEmpty()) {
            errors.add("Event date is required.");
        } else {
            try {
                LocalDate date = LocalDate.parse(eventDate.trim());
                if (!date.isAfter(LocalDate.now()))
                    errors.add("Event date must be a future date.");
            } catch (Exception e) {
                errors.add("Event date format is invalid (expected yyyy-MM-dd).");
            }
        }

        if (eventType == null || eventType.trim().isEmpty())
            errors.add("Event type is required.");
        else if (!ALLOWED_EVENT_TYPES.contains(eventType.trim().toUpperCase()))
            errors.add("Event type must be one of: Wedding, Birthday, Preshoot, Other.");

        if (!errors.isEmpty())
            throw new IllegalArgumentException(String.join(" ", errors));
    }

    // ── File I/O ────────────────────────────────────────────────────────────

    private Booking buildBooking(String id, String customerId, String customerName,
                                 String packageId, String packageName,
                                 String eventDate, String eventType,
                                 String status, String notes) {
        if ("WEDDING".equalsIgnoreCase(eventType))
            return new WeddingBooking(id, customerId, customerName, packageId, packageName, eventDate, status, notes);
        return new BirthdayBooking(id, customerId, customerName, packageId, packageName, eventDate, status, notes);
    }

    private void appendBooking(Booking b) {
        try {
            String line = formatLine(b) + System.lineSeparator();
            Files.write(Paths.get(filePath), line.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error saving booking", e);
        }
    }

    private void saveAll(List<Booking> list) {
        try {
            List<String> lines = list.stream().map(this::formatLine).collect(Collectors.toList());
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException("Error saving bookings", e);
        }
    }

    private String formatLine(Booking b) {
        return String.join(SEP,
                b.getId(),
                b.getCustomerId(),
                sanitise(b.getCustomerName()),
                b.getPackageId(),
                sanitise(b.getPackageName()),
                b.getEventDate(),
                b.getEventType(),
                b.getStatus(),
                sanitise(safe(b.getNotes())));
    }

    private String sanitise(String s) {
        return s == null ? "" : s.replace("|", "").replace("\n", " ").replace("\r", "");
    }

    private Booking parseLine(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 9) return null;
            return buildBooking(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8]);
        } catch (Exception e) { return null; }
    }

    private String safe(String s) { return s == null ? "" : s; }
}


