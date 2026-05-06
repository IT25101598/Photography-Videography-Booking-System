package com.reviewapp.model;

/**
 * User.java – Abstract base class (Component 01: User Management)
 *
 * OOP – ENCAPSULATION : all fields private, accessed via getters/setters
 * OOP – ABSTRACTION   : abstract class; subclasses provide getRole()
 * OOP – INHERITANCE   : AdminUser and CustomerUser extend this class
 */
public abstract class User {

    private String id;
    private String username;
    private String password;
    private String email;
    private String phone;

    public User() {}

    public User(String id, String username, String password, String email, String phone) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.email    = email;
        this.phone    = phone;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public String getId()                   { return id; }
    public void   setId(String id)          { this.id = id; }

    public String getUsername()             { return username; }
    public void   setUsername(String u)     { this.username = u; }

    public String getPassword()             { return password; }
    public void   setPassword(String p)     { this.password = p; }

    public String getEmail()                { return email; }
    public void   setEmail(String e)        { this.email = e; }

    public String getPhone()                { return phone; }
    public void   setPhone(String p)        { this.phone = p; }

    /**
     * OOP – POLYMORPHISM : subclasses return their own role string.
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return "User{id='" + id + "', username='" + username + "', role='" + getRole() + "'}";
    }
}

