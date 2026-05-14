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
        .hero-banner { background: linear-gradient(135deg,#343a40 0%,#495057 100%); color:white; padding:3rem 0; }
        .staff-card { border-top: 4px solid transparent; transition: transform .2s, box-shadow .2s; }
        .staff-card.PHOTOGRAPHER { border-top-color: #0d6efd; }
        .staff-card.VIDEOGRAPHER { border-top-color: #d63384; }
        .staff-card:hover { transform: translateY(-4px); box-shadow:0 .75rem 1.5rem rgba(0,0,0,.15)!important; }
    </style>
</head>
<body>
<%@ include file="_navbar.jsp" %>

<div class="hero-banner text-center">
    <h1 class="display-5 fw-bold"><i class="bi bi-people-fill me-2"></i>Our Team</h1>
    <p class="lead">Meet our talented photographers and videographers.</p>
</div>

<div class="container my-5">

    <c:if test="${not empty successMsg}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle-fill me-2"></i>${successMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMsg}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Filter + Admin Add Button -->
    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
        <div>
            <a href="/staff" class="btn btn-sm ${empty filterRole ? 'btn-dark' : 'btn-outline-dark'}">All</a>
            <a href="/staff?role=PHOTOGRAPHER" class="btn btn-sm ${filterRole == 'PHOTOGRAPHER' ? 'btn-primary' : 'btn-outline-primary'}">
                <i class="bi bi-camera me-1"></i>Photographers
            </a>
            <a href="/staff?role=VIDEOGRAPHER" class="btn btn-sm ${filterRole == 'VIDEOGRAPHER' ? 'btn-danger' : 'btn-outline-danger'}">
                <i class="bi bi-camera-video me-1"></i>Videographers
            </a>
        </div>
        <c:if test="${sessionScope.loggedInUser.role == 'ADMIN'}">
            <a href="/admin/staff/add" class="btn btn-success">
                <i class="bi bi-plus-circle me-1"></i>Add Staff
            </a>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${empty staffList}">
            <div class="text-center py-5">
                <i class="bi bi-person-slash display-1 text-muted"></i>
                <h4 class="mt-3 text-muted">No staff members found.</h4>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <c:forEach var="sm" items="${staffList}">
                    <div class="col">
                        <div class="card h-100 shadow-sm staff-card ${sm.staffRole}">
                            <div class="card-body text-center pt-4">
                                <div class="mb-3">
                                    <i class="bi bi-${sm.staffRole == 'PHOTOGRAPHER' ? 'camera-fill' : 'camera-video-fill'} display-4
                                        ${sm.staffRole == 'PHOTOGRAPHER' ? 'text-primary' : 'text-danger'}"></i>
                                </div>
                                <h5 class="fw-bold"><c:out value="${sm.name}"/></h5>
                                <span class="badge ${sm.staffRole == 'PHOTOGRAPHER' ? 'bg-primary' : 'bg-danger'} mb-2">
                                    ${sm.staffRole}
                                </span>
                                <p class="text-muted small"><c:out value="${sm.serviceDescription}"/></p>
                                <p class="mb-1"><i class="bi bi-envelope me-1 text-secondary"></i><c:out value="${sm.email}"/></p>
                                <c:if test="${not empty sm.phone}">
                                    <p class="mb-1"><i class="bi bi-telephone me-1 text-secondary"></i><c:out value="${sm.phone}"/></p>
                                </c:if>
                                <span class="badge ${sm.available ? 'bg-success' : 'bg-secondary'} mt-1">
                                    <i class="bi bi-${sm.available ? 'check-circle' : 'x-circle'} me-1"></i>
                                    ${sm.available ? 'Available' : 'Unavailable'}
                                </span>
                            </div>
                            <c:if test="${sessionScope.loggedInUser.role == 'ADMIN'}">
                                <div class="card-footer bg-transparent d-flex gap-2 justify-content-center">
                                    <a href="/admin/staff/edit/${sm.id}" class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-pencil me-1"></i>Edit
                                    </a>
                                    <form action="/admin/staff/delete/${sm.id}" method="post"
                                          onsubmit="return confirm('Delete staff member?')">
                                        <button class="btn btn-sm btn-outline-danger">
                                            <i class="bi bi-trash me-1"></i>Delete
                                        </button>
                                    </form>
                                </div>
                            </c:if>
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
</body>
</html>

