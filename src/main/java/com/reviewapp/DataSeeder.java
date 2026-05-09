package com.reviewapp;

import com.reviewapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * DataSeeder – runs once at startup and populates all flat files
 * with realistic sample data IF they are empty.
 *
 * Login credentials seeded:
 *   ADMIN    → admin / admin123
 *   CUSTOMER → kamal / kamal123
 *   CUSTOMER → nimal / nimal123
 *   CUSTOMER → saman / saman123
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired private UserService    userService;
    @Autowired private PackageService packageService;
    @Autowired private StaffService   staffService;
    @Autowired private BookingService bookingService;
    @Autowired private ReviewService  reviewService;

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
        seedPackages();
        seedStaff();
        seedBookings();
        seedReviews();
        log.info("=== DataSeeder complete ===");
    }

    // ── Users ────────────────────────────────────────────────────────────────

    private void seedUsers() {
        if (!userService.getAllUsers().isEmpty()) return;
        log.info("Seeding users...");
        try {
            userService.register("admin",  "admin123",  "admin@lensstudio.lk",  "+94 11 234 5678", "ADMIN");
            userService.register("kamal",  "kamal123",  "kamal@gmail.com",      "+94 77 111 2233", "CUSTOMER");
            userService.register("nimal",  "nimal123",  "nimal@gmail.com",      "+94 76 222 3344", "CUSTOMER");
            userService.register("saman",  "saman123",  "saman@gmail.com",      "+94 71 333 4455", "CUSTOMER");
            userService.register("dilini", "dilini123", "dilini@gmail.com",     "+94 78 444 5566", "CUSTOMER");
            log.info("Users seeded: admin, kamal, nimal, saman, dilini");
        } catch (Exception e) {
            log.warn("User seed error: {}", e.getMessage());
        }
    }

    // ── Packages ─────────────────────────────────────────────────────────────

    private void seedPackages() {
        if (!packageService.getAllPackages().isEmpty()) return;
        log.info("Seeding packages...");
        try {
            packageService.addPackage(
                "Wedding Silver",  "PHOTO",
                "Full-day wedding photography coverage with edited album of 300 photos.",
                75000, 10);
            packageService.addPackage(
                "Wedding Gold",    "PHOTO",
                "Premium wedding photography with drone shots, same-day highlights and 500 photos.",
                120000, 12);
            packageService.addPackage(
                "Wedding Platinum","VIDEO",
                "Cinematic wedding videography with 4K drone footage, teaser and full film.",
                150000, 12);
            packageService.addPackage(
                "Birthday Basic",  "PHOTO",
                "Fun birthday photography session – 2 hours, 100 edited photos.",
                18000, 2);
            packageService.addPackage(
                "Birthday Premium","VIDEO",
                "Birthday videography with highlights reel and raw footage.",
                35000, 4);
            packageService.addPackage(
                "Preshoot Classic","PHOTO",
                "Romantic pre-shoot session at a scenic location, 150 edited photos.",
                25000, 3);
            packageService.addPackage(
                "Preshoot Cinematic","VIDEO",
                "Cinematic pre-shoot short film, 2–3 minute film with colour grading.",
                45000, 4);
            packageService.addPackage(
                "Corporate Event", "PHOTO",
                "Professional corporate event photography – up to 6 hours coverage.",
                50000, 6);
            log.info("Packages seeded.");
        } catch (Exception e) {
            log.warn("Package seed error: {}", e.getMessage());
        }
    }

    // ── Staff ────────────────────────────────────────────────────────────────

    private void seedStaff() {
        if (!staffService.getAllStaff().isEmpty()) return;
        log.info("Seeding staff...");
        try {
            staffService.addStaff("Kasun Perera",   "kasun@lensstudio.lk",   "+94 77 100 2001", "PHOTOGRAPHER");
            staffService.addStaff("Sanduni Silva",  "sanduni@lensstudio.lk", "+94 76 100 2002", "PHOTOGRAPHER");
            staffService.addStaff("Ruwan Fernando", "ruwan@lensstudio.lk",   "+94 71 100 2003", "PHOTOGRAPHER");
            staffService.addStaff("Thilini Jayawardena", "thilini@lensstudio.lk", "+94 78 100 2004", "VIDEOGRAPHER");
            staffService.addStaff("Chathura Bandara",    "chathura@lensstudio.lk","+94 77 100 2005", "VIDEOGRAPHER");
            staffService.addStaff("Amali Gunaratne",     "amali@lensstudio.lk",   "+94 76 100 2006", "PHOTOGRAPHER");
            log.info("Staff seeded.");
        } catch (Exception e) {
            log.warn("Staff seed error: {}", e.getMessage());
        }
    }

    // ── Bookings ─────────────────────────────────────────────────────────────

    private void seedBookings() {
        if (!bookingService.getAllBookings().isEmpty()) return;
        log.info("Seeding bookings...");
        try {
            // Get the customer user IDs
            var kamal  = userService.findByUsername("kamal");
            var nimal  = userService.findByUsername("nimal");
            var saman  = userService.findByUsername("saman");
            var dilini = userService.findByUsername("dilini");
            var pkgs   = packageService.getAllPackages();

            if (kamal != null && pkgs.size() >= 1) {
                bookingService.addBooking(
                    kamal.getId(), kamal.getUsername(),
                    pkgs.get(0).getId(), pkgs.get(0).getName(),
                    "2026-06-15", "WEDDING", "Please bring natural light reflectors.");
            }
            if (nimal != null && pkgs.size() >= 4) {
                bookingService.addBooking(
                    nimal.getId(), nimal.getUsername(),
                    pkgs.get(3).getId(), pkgs.get(3).getName(),
                    "2026-05-10", "BIRTHDAY", "Kids birthday party – outdoor garden.");
            }
            if (saman != null && pkgs.size() >= 2) {
                bookingService.addBooking(
                    saman.getId(), saman.getUsername(),
                    pkgs.get(2).getId(), pkgs.get(2).getName(),
                    "2026-07-20", "WEDDING", "Church ceremony followed by reception.");
            }
            if (dilini != null && pkgs.size() >= 5) {
                bookingService.addBooking(
                    dilini.getId(), dilini.getUsername(),
                    pkgs.get(5).getId(), pkgs.get(5).getName(),
                    "2026-05-28", "PRESHOOT", "Sunset beach preshoot.");
            }

            // Update one booking to CONFIRMED
            var allBookings = bookingService.getAllBookings();
            if (!allBookings.isEmpty()) {
                bookingService.updateStatus(allBookings.get(0).getId(), "CONFIRMED");
            }
            log.info("Bookings seeded.");
        } catch (Exception e) {
            log.warn("Booking seed error: {}", e.getMessage());
        }
    }

    // ── Reviews ──────────────────────────────────────────────────────────────

    private void seedReviews() {
        if (!reviewService.getAllReviews().isEmpty()) return;
        log.info("Seeding reviews...");
        try {
            var pkgs = packageService.getAllPackages();
            if (pkgs.isEmpty()) return;

            String p0 = pkgs.get(0).getId();
            String p1 = pkgs.size() > 1 ? pkgs.get(1).getId() : p0;
            String p3 = pkgs.size() > 3 ? pkgs.get(3).getId() : p0;
            String p5 = pkgs.size() > 5 ? pkgs.get(5).getId() : p0;

            reviewService.addReview(p0, "Kamal Perera",
                "Absolutely stunning photos! The team was professional and captured every moment perfectly. " +
                "We couldn't be happier with our wedding album.", 5);
            reviewService.addReview(p1, "Sandali Fernando",
                "The gold wedding package exceeded our expectations. Drone shots were breathtaking " +
                "and the same-day highlights brought tears to our eyes.", 5);
            reviewService.addReview(p3, "Nimal Jayawardena",
                "Great birthday photos for my daughter's party. The photographer kept the kids engaged " +
                "and the results were colourful and joyful.", 4);
            reviewService.addReview(p5, "Amara Silva",
                "The preshoot was so much fun! The photographer had amazing ideas for poses and locations. " +
                "Highly recommend the Classic Preshoot package.", 5);
            reviewService.addReview(p0, "Thilak Bandara",
                "Very professional service from start to finish. Booking was easy and the team arrived on time. " +
                "Photo quality is excellent.", 4);
            reviewService.addReview(p1, "Dilini Rathnayake",
                "Worth every rupee. The edited photos were delivered within two weeks and the album quality " +
                "is top notch. Will definitely recommend LensStudio.", 5);
            log.info("Reviews seeded.");
        } catch (Exception e) {
            log.warn("Review seed error: {}", e.getMessage());
        }
    }
}

