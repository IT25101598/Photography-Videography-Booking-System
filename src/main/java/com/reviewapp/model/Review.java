package com.reviewapp.model;
/**
 * Review.java - Abstract Base Class
 *
 * OOP PRINCIPLE: ENCAPSULATION
 * All fields are declared PRIVATE. External code accesses them
 * only through PUBLIC getter/setter methods.
 *
 * OOP PRINCIPLE: ABSTRACTION
 * The class is declared ABSTRACT - it cannot be instantiated
 * directly. Subclasses must implement getDisplayFormat().
 */
public abstract class Review {
    // ENCAPSULATION: private fields
    private String id;
    private String packageId;
    private String authorName;
    private String content;
    private int    rating;
    /** No-arg constructor */
    public Review() {}
    /** Full constructor */
    public Review(String id, String packageId, String authorName,
                  String content, int rating) {
        this.id         = id;
        this.packageId  = packageId;
        this.authorName = authorName;
        this.content    = content;
        this.rating     = rating;
    }
    // ENCAPSULATION: public getters and setters
    public String getId()                 { return id; }
    public void   setId(String id)        { this.id = id; }
    public String getPackageId()          { return packageId; }
    public void   setPackageId(String p)  { this.packageId = p; }
    public String getAuthorName()         { return authorName; }
    public void   setAuthorName(String a) { this.authorName = a; }
    public String getContent()            { return content; }
    public void   setContent(String c)    { this.content = c; }
    public int    getRating()             { return rating; }
    public void   setRating(int r)        { this.rating = r; }
    /** Generates a star string e.g. rating=3 gives 3 filled + 2 empty stars */
    protected String buildStars() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append(i <= rating ? "&#9733;" : "&#9734;");
        }
        return sb.toString();
    }
    /** Short preview of content (max 60 chars) - callable via JSP EL */
    public String getContentPreview() {
        if (content == null) return "";
        return content.length() > 60 ? content.substring(0, 60) + "..." : content;
    }
    /**
     * Returns the type string. Overridden by VerifiedReview.
     * Used in JSP EL as ${review.reviewType}
     */
    public String getReviewType() {
        return "PUBLIC";
    }
    /**
     * EL-safe no-arg wrapper for getDisplayFormat(false).
     * JSP EL cannot pass boolean literals, so use ${review.publicDisplayFormat}
     */
    public String getPublicDisplayFormat() {
        return getDisplayFormat(false);
    }
    /**
     * EL-safe no-arg wrapper for getDisplayFormat(true).
     * Use ${review.adminDisplayFormat} in admin JSP.
     */
    public String getAdminDisplayFormat() {
        return getDisplayFormat(true);
    }
    /**
     * POLYMORPHISM: Abstract method - each subclass provides its own
     * HTML card layout. Runtime Polymorphism (Dynamic Dispatch) means
     * the JVM calls the correct subclass version at runtime.
     *
     * @param isAdmin true = admin view, false = public view
     * @return HTML string rendered directly in JSP
     */
    public abstract String getDisplayFormat(boolean isAdmin);
    @Override
    public String toString() {
        return "Review{id='" + id + "', packageId='" + packageId
             + "', author='" + authorName + "', rating=" + rating + "}";
    }
}