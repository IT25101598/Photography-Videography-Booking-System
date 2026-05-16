<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${pageTitle} – LensStudio</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>
    <style>
        body { background-color: #f0f2f5; }
        .hero-banner {
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
            color: white; padding: 2.5rem 0;
        }
        .stat-card {
            border: none; border-radius: 1rem;
            transition: transform .2s, box-shadow .2s;
        }
        .stat-card:hover { transform: translateY(-5px); box-shadow: 0 1rem 2rem rgba(0,0,0,.15)!important; }
        .stat-icon { font-size: 2.5rem; opacity: .9; }
        .quick-link-card { border-radius: .75rem; transition: transform .15s; }
        .quick-link-card:hover { transform: scale(1.03); }
    </style>
</head>
<body>
<%@ include file="_navbar.jsp" %>

<!-- ── Hero Banner ────────────────────────────────────────── -->
<div class="hero-banner text-center">
    <h1 class="display-5 fw-bold">
        <i class="bi bi-speedometer2 me-2"></i>
        <c:choose>
            <c:when test="${user.role == 'ADMIN'}">Admin Dashboard</c:when>
            <c:otherwise>Welcome, <c:out value="${user.username}"/>!</c:otherwise>
        </c:choose>
    </h1>
    <p class="lead opacity-75">LensStudio Photography &amp; Videography Management System</p>
</div>

<div class="container my-5">

    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- ── Stats Row (Admin only) ──────────────────────────────────── -->
    <c:if test="${user.role == 'ADMIN'}">
        <h5 class="fw-bold text-muted mb-3"><i class="bi bi-bar-chart-fill me-2"></i>System Overview</h5>
        <div class="row row-cols-2 row-cols-md-4 g-3 mb-5">

            <!-- Users -->
            <div class="col">
                <div class="card stat-card shadow text-white" style="background:linear-gradient(135deg,#4e54c8,#8f94fb)">
                    <div class="card-body text-center py-4">
                        <i class="bi bi-people-fill stat-icon"></i>
                        <h2 class="display-6 fw-bold mb-0">${stats.totalUsers}</h2>
                        <p class="mb-0 opacity-75">Total Users</p>
                    </div>
                    <div class="card-footer bg-transparent border-0 text-center pb-3">
                        <a href="/users" class="btn btn-sm btn-light opacity-90">Manage →</a>
                    </div>
                </div>
            </div>

            <!-- Packages -->
            <div class="col">
                <div class="card stat-card shadow text-white" style="background:linear-gradient(135deg,#11998e,#38ef7d)">
                    <div class="card-body text-center py-4">
                        <i class="bi bi-box-seam stat-icon"></i>
                        <h2 class="display-6 fw-bold mb-0">${stats.totalPackages}</h2>
                        <p class="mb-0 opacity-75">Packages</p>
                        <small class="opacity-75">${stats.availablePackages} available</small>
                    </div>
                    <div class="card-footer bg-transparent border-0 text-center pb-3">
                        <a href="/admin/packages" class="btn btn-sm btn-light opacity-90">Manage →</a>
                    </div>
                </div>
            </div>

            <!-- Bookings -->
            <div class="col">
                <div class="card stat-card shadow text-white" style="background:linear-gradient(135deg,#f7971e,#ffd200)">
                    <div class="card-body text-center py-4">
                        <i class="bi bi-calendar-check stat-icon"></i>
                        <h2 class="display-6 fw-bold mb-0">${stats.totalBookings}</h2>
                        <p class="mb-0 opacity-75">Bookings</p>
                        <small class="opacity-75">${stats.pendingBookings} pending</small>
                    </div>
                    <div class="card-footer bg-transparent border-0 text-center pb-3">
                        <a href="/admin/bookings" class="btn btn-sm btn-light opacity-90">Manage →</a>
                    </div>
                </div>
            </div>

            <!-- Staff -->
            <div class="col">
                <div class="card stat-card shadow text-white" style="background:linear-gradient(135deg,#c94b4b,#4b134f)">
                    <div class="card-body text-center py-4">
                        <i class="bi bi-person-badge-fill stat-icon"></i>
                        <h2 class="display-6 fw-bold mb-0">${stats.totalStaff}</h2>
                        <p class="mb-0 opacity-75">Staff</p>
                        <small class="opacity-75">${stats.availableStaff} available</small>
                    </div>
                    <div class="card-footer bg-transparent border-0 text-center pb-3">
                        <a href="/staff" class="btn btn-sm btn-light opacity-90">Manage →</a>
                    </div>
                </div>
            </div>

            <!-- Reviews -->
            <div class="col">
                <div class="card stat-card shadow text-white" style="background:linear-gradient(135deg,#0d6efd,#6ea8fe)">
                    <div class="card-body text-center py-4">
                        <i class="bi bi-star-fill stat-icon"></i>
                        <h2 class="display-6 fw-bold mb-0">${stats.totalReviews}</h2>
                        <p class="mb-0 opacity-75">Reviews</p>
                    </div>
                    <div class="card-footer bg-transparent border-0 text-center pb-3">
                        <a href="/adminModeration" class="btn btn-sm btn-light opacity-90">Moderate →</a>
                    </div>
                </div>
            </div>

        </div>
    </c:if>

    <!-- ── Quick Links ────────────────────────────────────────────── -->
    <h5 class="fw-bold text-muted mb-3"><i class="bi bi-lightning-charge me-2"></i>Quick Links</h5>
    <div class="row row-cols-2 row-cols-md-3 row-cols-lg-4 g-3">

        <div class="col">
            <a href="/packages" class="text-decoration-none">
                <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                    <i class="bi bi-box-seam display-5 text-primary mb-2"></i>
                    <strong>View Packages</strong>
                    <p class="text-muted small mb-0">Browse photography &amp; video packages</p>
                </div>
            </a>
        </div>

        <c:if test="${not empty sessionScope.loggedInUser}">
            <div class="col">
                <a href="/bookings/new" class="text-decoration-none">
                    <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                        <i class="bi bi-calendar-plus display-5 text-success mb-2"></i>
                        <strong>Book a Package</strong>
                        <p class="text-muted small mb-0">Reserve your event slot</p>
                    </div>
                </a>
            </div>
            <div class="col">
                <a href="/bookings/my" class="text-decoration-none">
                    <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                        <i class="bi bi-calendar-check display-5 text-warning mb-2"></i>
                        <strong>My Bookings</strong>
                        <p class="text-muted small mb-0">View &amp; manage your bookings</p>
                    </div>
                </a>
            </div>
        </c:if>

        <div class="col">
            <a href="/staff" class="text-decoration-none">
                <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                    <i class="bi bi-people-fill display-5 text-dark mb-2"></i>
                    <strong>Our Team</strong>
                    <p class="text-muted small mb-0">Meet our photographers &amp; videographers</p>
                </div>
            </a>
        </div>

        <div class="col">
            <a href="/viewReviews" class="text-decoration-none">
                <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                    <i class="bi bi-star-fill display-5 text-warning mb-2"></i>
                    <strong>Reviews</strong>
                    <p class="text-muted small mb-0">See what customers say</p>
                </div>
            </a>
        </div>

        <div class="col">
            <a href="/submitReview" class="text-decoration-none">
                <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                    <i class="bi bi-pencil-square display-5 text-info mb-2"></i>
                    <strong>Write a Review</strong>
                    <p class="text-muted small mb-0">Share your experience</p>
                </div>
            </a>
        </div>

        <c:if test="${empty sessionScope.loggedInUser}">
            <div class="col">
                <a href="/login" class="text-decoration-none">
                    <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                        <i class="bi bi-box-arrow-in-right display-5 text-primary mb-2"></i>
                        <strong>Login</strong>
                        <p class="text-muted small mb-0">Sign in to your account</p>
                    </div>
                </a>
            </div>
            <div class="col">
                <a href="/register" class="text-decoration-none">
                    <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                        <i class="bi bi-person-plus display-5 text-success mb-2"></i>
                        <strong>Register</strong>
                        <p class="text-muted small mb-0">Create a new account</p>
                    </div>
                </a>
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.loggedInUser}">
            <div class="col">
                <a href="/profile" class="text-decoration-none">
                    <div class="card quick-link-card shadow-sm text-center p-3 h-100">
                        <i class="bi bi-person-circle display-5 text-secondary mb-2"></i>
                        <strong>My Profile</strong>
                        <p class="text-muted small mb-0">Update your details</p>
                    </div>
                </a>
            </div>
        </c:if>

    </div>

    <%-- ── Announcements (visible to all) ──────────────────────────────────── --%>
    <div class="mt-5">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-bold text-muted mb-0">
                <i class="bi bi-megaphone-fill me-2"></i>Announcements
            </h5>
            <c:if test="${user.role == 'ADMIN'}">
                <a href="/admin/announcements" class="btn btn-sm btn-outline-primary">
                    <i class="bi bi-gear me-1"></i>Manage
                </a>
            </c:if>
        </div>
        <c:choose>
            <c:when test="${empty announcements}">
                <div class="alert alert-light border text-muted text-center">
                    <i class="bi bi-megaphone me-1"></i>No announcements at this time.
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-3">
                    <c:forEach var="a" items="${announcements}">
                        <div class="col-12 col-md-6 col-lg-4">
                            <div class="card h-100 shadow-sm border-0"
                                 style="border-left: 4px solid
                                 <c:choose>
                                     <c:when test="${a.priority == 'HIGH'}">#dc3545</c:when>
                                     <c:when test="${a.priority == 'MEDIUM'}">#ffc107</c:when>
                                     <c:otherwise>#6c757d</c:otherwise>
                                 </c:choose>!important;">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                        <h6 class="fw-bold mb-0"><c:out value="${a.title}"/></h6>
                                        <span class="badge bg-${a.priorityBadgeClass} ms-2">${a.priority}</span>
                                    </div>
                                    <p class="text-muted small mb-1"><c:out value="${a.message}"/></p>
                                    <small class="text-muted">
                                        <i class="bi bi-calendar3 me-1"></i>${a.createdDate}
                                    </small>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>

<footer class="bg-dark text-white-50 text-center py-3 mt-5">
    <small>&copy; 2026 LensStudio &mdash; OOP Project | Spring Boot + JSP</small>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

