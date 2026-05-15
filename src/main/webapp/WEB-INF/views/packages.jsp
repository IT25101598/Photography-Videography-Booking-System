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
        body { background-color: #f8f9fa; }
        .hero-banner { background: linear-gradient(135deg,#6f42c1 0%,#d63384 100%); color:white; padding:3rem 0; }
        .pkg-card { transition: transform .2s, box-shadow .2s; border-top: 4px solid transparent; }
        .pkg-card.PHOTO { border-top-color: #0d6efd; }
        .pkg-card.VIDEO { border-top-color: #d63384; }
        .pkg-card:hover { transform: translateY(-4px); box-shadow: 0 .75rem 1.5rem rgba(0,0,0,.15)!important; }
    </style>
</head>
<body>
<%@ include file="_navbar.jsp" %>

<div class="hero-banner text-center">
    <h1 class="display-5 fw-bold"><i class="bi bi-box-seam me-2"></i>Our Packages</h1>
    <p class="lead">Choose the perfect photography or videography package for your special event.</p>
</div>

<div class="container my-5">

    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Search -->
    <div class="card shadow-sm mb-4 p-3">
        <form action="/packages" method="get" class="row g-2 align-items-end">
            <div class="col-md-8">
                <label for="query" class="form-label fw-semibold"><i class="bi bi-search me-1"></i>Search Packages</label>
                <input type="text" class="form-control" id="query" name="query"
                       placeholder="Search by name, type or description..."
                       value="<c:out value='${query}'/>"/>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="/packages" class="btn btn-outline-secondary ms-1">Clear</a>
            </div>
        </form>
    </div>

    <!-- Package Grid -->
    <c:choose>
        <c:when test="${empty packages}">
            <div class="text-center py-5">
                <i class="bi bi-box display-1 text-muted"></i>
                <h4 class="mt-3 text-muted">No packages found.</h4>
            </div>
        </c:when>
        <c:otherwise>
            <p class="text-muted mb-3"><strong>${packages.size()}</strong> package(s) found.</p>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <c:forEach var="pkg" items="${packages}">
                    <div class="col">
                        <div class="card h-100 shadow-sm pkg-card ${pkg.packageType}">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <span class="fw-bold"><c:out value="${pkg.name}"/></span>
                                <span class="badge ${pkg.packageType == 'PHOTO' ? 'bg-primary' : 'bg-pink text-white'}"
                                      style="${pkg.packageType == 'VIDEO' ? 'background:#d63384' : ''}">
                                    <i class="bi bi-${pkg.packageType == 'PHOTO' ? 'camera' : 'camera-video'} me-1"></i>${pkg.packageType}
                                </span>
                            </div>
                            <div class="card-body">
                                <p class="card-text text-muted"><c:out value="${pkg.description}"/></p>
                                <p class="mb-1"><i class="bi bi-clock me-1 text-secondary"></i>${pkg.durationHours} hours</p>
                                <p class="mb-0 fs-5 fw-bold text-success">Rs. <c:out value="${pkg.price}"/></p>
                                <small class="text-muted"><c:out value="${pkg.packageDetails}"/></small>
                            </div>
                            <div class="card-footer bg-transparent">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.loggedInUser}">
                                        <a href="/bookings/new?packageId=${pkg.id}" class="btn btn-sm btn-primary w-100">
                                            <i class="bi bi-calendar-plus me-1"></i>Book Now
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/login" class="btn btn-sm btn-outline-primary w-100">
                                            <i class="bi bi-box-arrow-in-right me-1"></i>Login to Book
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<footer class="bg-dark text-white-50 text-center py-3 mt-5">
    <small>&copy; 2026 LensStudio &mdash; OOP Project | Spring Boot + JSP</small>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Sanitise search query: strip characters that could break the flat-file separator
    document.querySelector('form[action="/packages"]').addEventListener('submit', function(e) {
        const el = document.getElementById('query');
        el.value = el.value.replace(/[|<>]/g, '').trim().substring(0, 100);
    });
</script>
</body>
</html>

