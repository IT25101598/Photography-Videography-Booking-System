package com.reviewapp.model;

/**
 * Staff.java – Base class (Component 04: Staff Management)
 *
 * OOP – ENCAPSULATION : private fields + getters/setters
 * OOP – ABSTRACTION   : abstract; subclasses implement getServiceDescription()
 * OOP – INHERITANCE   : Photographer and Videographer extend this
 */
public abstract class Staff {

    private String  id;
    private String  name;
    private String  email;
    private String  phone;
    private boolean available;

    public Staff() {}

    public Staff(String id, String name, String email,
                 String phone, boolean available) {
        this.id        = id;
        this.name      = name;
        this.email     = email;
        this.phone     = phone;
        this.available = available;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public String  getId()                  { return id; }
    public void    setId(String id)         { this.id = id; }

    public String  getName()                { return name; }
    public void    setName(String name)     { this.name = name; }

    public String  getEmail()               { return email; }
    public void    setEmail(String e)       { this.email = e; }

    public String  getPhone()               { return phone; }
    public void    setPhone(String p)       { this.phone = p; }

    public boolean isAvailable()            { return available; }
    public void    setAvailable(boolean a)  { this.available = a; }

    /** OOP – POLYMORPHISM : subclasses return their staff role */
    public abstract String getStaffRole();

    /** OOP – POLYMORPHISM : subclasses describe their service */
    public abstract String getServiceDescription();

    @Override
    public String toString() {
        return "Staff{id='" + id + "', name='" + name
                + "', role='" + getStaffRole() + "'}";
    }
}

