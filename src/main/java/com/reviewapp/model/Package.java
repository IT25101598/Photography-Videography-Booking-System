package com.reviewapp.model;

/**
 * Package.java – Base class (Component 02: Package Management)
 *
 * OOP – ENCAPSULATION : private fields, public getters/setters
 * OOP – ABSTRACTION   : abstract; subclasses implement getPackageDetails()
 * OOP – INHERITANCE   : PhotoPackage and VideoPackage extend this
 */
public abstract class Package {

    private String  id;
    private String  name;
    private String  description;
    private double  price;
    private int     durationHours;
    private boolean available;

    public Package() {}

    public Package(String id, String name, String description,
                   double price, int durationHours, boolean available) {
        this.id            = id;
        this.name          = name;
        this.description   = description;
        this.price         = price;
        this.durationHours = durationHours;
        this.available     = available;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────
    public String  getId()                      { return id; }
    public void    setId(String id)             { this.id = id; }

    public String  getName()                    { return name; }
    public void    setName(String name)         { this.name = name; }

    public String  getDescription()             { return description; }
    public void    setDescription(String d)     { this.description = d; }

    public double  getPrice()                   { return price; }
    public void    setPrice(double price)       { this.price = price; }

    public int     getDurationHours()           { return durationHours; }
    public void    setDurationHours(int d)      { this.durationHours = d; }

    public boolean isAvailable()                { return available; }
    public void    setAvailable(boolean a)      { this.available = a; }

    /** OOP – POLYMORPHISM : subclasses return their type string */
    public abstract String getPackageType();

    /** OOP – POLYMORPHISM : subclasses can override pricing logic */
    public double calculateFinalPrice() { return price; }

    /** OOP – POLYMORPHISM : subclasses provide their service details */
    public abstract String getPackageDetails();

    @Override
    public String toString() {
        return "Package{id='" + id + "', name='" + name
                + "', type='" + getPackageType() + "', price=" + price + "}";
    }
}

