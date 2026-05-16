package com.reviewapp.model;

/**
 * Announcement – Component 05: Dashboard Integration
 *
 * OOP – ENCAPSULATION : all fields private, accessed via getters/setters.
 * File format: id|title|message|priority|createdDate
 */
public abstract class Announcement {

    private String id;
    private String title;
    private String message;
    private String priority;   // HIGH | MEDIUM | LOW
    private String createdDate;

    // ── Constructors ─────────────────────────────────────────────────────────

    public Announcement() {}

    public Announcement(String id, String title, String message,
                        String priority, String createdDate) {
        this.id          = id;
        this.title       = title;
        this.message     = message;
        this.priority    = priority;
        this.createdDate = createdDate;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public String getId()          { return id; }
    public void   setId(String id) { this.id = id; }

    public String getTitle()             { return title; }
    public void   setTitle(String title) { this.title = title; }

    public String getMessage()               { return message; }
    public void   setMessage(String message) { this.message = message; }

    public String getPriority()                { return priority; }
    public void   setPriority(String priority) { this.priority = priority; }

    public String getCreatedDate()                   { return createdDate; }
    public void   setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    // ── Helper ───────────────────────────────────────────────────────────────

    /** Returns a Bootstrap badge colour class based on priority. */
    public abstract String getPriorityBadgeClass();
}
