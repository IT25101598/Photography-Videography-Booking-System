package com.reviewapp.service;

import com.reviewapp.model.PublicReview;
import com.reviewapp.model.Review;
import com.reviewapp.model.VerifiedReview;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private static final String SEPARATOR = "|";

    @Value("${reviews.file.path}")
    private String filePath;

    public List<Review> getAllReviews() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<Review> reviews = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                Review review = parseLine(line);
                if (review != null) {
                    reviews.add(review);
                }
            }
            return reviews;
        } catch (IOException e) {
            throw new RuntimeException("Error reading reviews file", e);
        }
    }

    public List<Review> getReviewsByPackageId(String packageId) {
        return getAllReviews().stream()
                .filter(r -> r.getPackageId().equalsIgnoreCase(packageId))
                .collect(Collectors.toList());
    }

    /**
     * Full-text search across author name, review content and package ID.
     * Case-insensitive, matches any word.
     */
    /**
     * Full-text search across author name, review content and package ID.
     * Optionally filter by minimum rating and review type.
     */
    public List<Review> search(String query) {
        return search(query, 0, null);
    }

    public List<Review> search(String query, int minRating, String reviewType) {
        String q = (query != null) ? query.trim().toLowerCase() : "";
        return getAllReviews().stream()
                .filter(r -> {
                    // text filter
                    boolean textMatch = q.isEmpty()
                            || r.getAuthorName().toLowerCase().contains(q)
                            || r.getContent().toLowerCase().contains(q)
                            || r.getPackageId().toLowerCase().contains(q);
                    // rating filter
                    boolean ratingMatch = (minRating <= 0) || r.getRating() >= minRating;
                    // type filter
                    boolean typeMatch = (reviewType == null || reviewType.isEmpty())
                            || r.getReviewType().equalsIgnoreCase(reviewType);
                    return textMatch && ratingMatch && typeMatch;
                })
                .collect(Collectors.toList());
    }

    public Review getReviewById(String id) {
        return getAllReviews().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // ── Validation helper ────────────────────────────────────────────────
    private void validateReviewInput(String packageId, String authorName,
                                     String content, int rating) {
        List<String> errors = new ArrayList<>();

        if (packageId == null || packageId.trim().isEmpty()) {
            errors.add("Package ID is required.");
        } else if (packageId.trim().length() > 50) {
            errors.add("Package ID must not exceed 50 characters.");
        } else if (!packageId.trim().matches("[A-Za-z0-9\\-_]+")) {
            errors.add("Package ID may only contain letters, digits, hyphens and underscores.");
        }

        if (authorName == null || authorName.trim().isEmpty()) {
            errors.add("Author name is required.");
        } else if (authorName.trim().length() > 100) {
            errors.add("Author name must not exceed 100 characters.");
        } else if (!authorName.trim().matches("[A-Za-z ]+")) {
            errors.add("Author name may only contain letters and spaces.");
        }

        if (content == null || content.trim().isEmpty()) {
            errors.add("Review content is required.");
        } else if (content.trim().length() < 10) {
            errors.add("Review content must be at least 10 characters.");
        } else if (content.trim().length() > 1000) {
            errors.add("Review content must not exceed 1000 characters.");
        }

        if (rating < 1 || rating > 5) {
            errors.add("Rating must be between 1 and 5.");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(" ", errors));
        }
    }

    public void addReview(String packageId, String authorName, String content, int rating) {
        validateReviewInput(packageId, authorName, content, rating);
        String id = UUID.randomUUID().toString();
        Review review = new PublicReview(id, packageId, authorName, content, rating);
        saveReview(review);
    }

    public void updateReview(String id, String packageId, String authorName,
                           String content, int rating, String reviewType) {
        validateReviewInput(packageId, authorName, content, rating);
        List<Review> reviews = getAllReviews();
        reviews.removeIf(r -> r.getId().equals(id));

        Review updatedReview;
        if ("VERIFIED".equalsIgnoreCase(reviewType)) {
            updatedReview = new VerifiedReview(id, packageId, authorName, content, rating);
        } else {
            updatedReview = new PublicReview(id, packageId, authorName, content, rating);
        }

        reviews.add(updatedReview);
        saveAllReviews(reviews);
    }

    public void deleteReview(String id) {
        List<Review> reviews = getAllReviews();
        reviews.removeIf(r -> r.getId().equals(id));
        saveAllReviews(reviews);
    }

    private void saveReview(Review review) {
        try {
            String line = formatReviewLine(review);
            Files.write(Paths.get(filePath), (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error saving review", e);
        }
    }

    private void saveAllReviews(List<Review> reviews) {
        try {
            List<String> lines = reviews.stream()
                    .map(this::formatReviewLine)
                    .collect(Collectors.toList());
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            throw new RuntimeException("Error saving reviews", e);
        }
    }

    private String formatReviewLine(Review review) {
        return String.join(SEPARATOR,
                review.getId(),
                review.getPackageId(),
                review.getReviewType(),
                sanitise(review.getAuthorName()),
                sanitise(review.getContent()),
                String.valueOf(review.getRating())
        );
    }

    /** Strip the pipe separator character to protect the flat-file format */
    private String sanitise(String s) {
        return s == null ? "" : s.replace("|", "").replace("\n", " ").replace("\r", "");
    }

    private Review parseLine(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 6) return null;

            String id = parts[0];
            String packageId = parts[1];
            String type = parts[2];
            String authorName = parts[3];
            String content = parts[4];
            int rating = Integer.parseInt(parts[5]);

            if ("VERIFIED".equalsIgnoreCase(type)) {
                return new VerifiedReview(id, packageId, authorName, content, rating);
            } else {
                return new PublicReview(id, packageId, authorName, content, rating);
            }
        } catch (Exception e) {
            return null;
        }
    }
}

