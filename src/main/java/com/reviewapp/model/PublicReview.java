package com.reviewapp.model;
/**
 * PublicReview.java - Concrete subclass of Review
 *
 * OOP PRINCIPLE: INHERITANCE
 * PublicReview EXTENDS Review, inheriting all fields and
 * methods (IS-A relationship: a PublicReview IS-A Review).
 *
 * OOP PRINCIPLE: POLYMORPHISM (Method Overriding)
 * Provides its own implementation of getDisplayFormat().
 * When called on a Review reference holding a PublicReview
 * object, THIS version executes - Runtime Polymorphism.
 */
public class PublicReview extends Review {
    public PublicReview() {
        super();
    }
    public PublicReview(String id, String packageId, String authorName,
                        String content, int rating) {
        super(id, packageId, authorName, content, rating);
    }
    /**
     * POLYMORPHISM: Override of abstract method.
     * Renders a standard Bootstrap card for a public review.
     * isAdmin=false -> user view (no extra badges)
     * isAdmin=true  -> shows "Public Review" badge for admin
     */
    @Override
    public String getDisplayFormat(boolean isAdmin) {
        String stars = buildStars();
        String badge = isAdmin
            ? "<span class=\"badge bg-secondary ms-2\">Public Review</span>"
            : "";
        return "<div class=\"card mb-3 border-secondary\">"
             + "<div class=\"card-header d-flex justify-content-between align-items-center\">"
             + "<span><strong>" + getAuthorName() + "</strong>" + badge + "</span>"
             + "<span class=\"text-warning\">" + stars + "</span>"
             + "</div>"
             + "<div class=\"card-body\">"
             + "<p class=\"card-text\">" + getContent() + "</p>"
             + "<small class=\"text-muted\">Package: " + getPackageId() + "</small>"
             + (isAdmin ? "<br><small class=\"text-muted\">ID: " + getId() + "</small>" : "")
             + "</div>"
             + "</div>";
    }
}