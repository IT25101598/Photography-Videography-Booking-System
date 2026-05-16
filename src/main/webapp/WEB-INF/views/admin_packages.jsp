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
    <style> body { background-color: #f8f9fa; } </style>
</head>
<body>
<%@ include file="_navbar.jsp" %>

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold"><i class="bi bi-box-seam text-primary me-2"></i>Manage Packages</h2>
        <a href="/admin/packages/add" class="btn btn-success">
            <i class="bi bi-plus-circle me-1"></i>Add Package
        </a>
    </div>

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

    <div class="card shadow">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th>#</th><th>Name</th><th>Type</th><th>Price (Rs.)</th>
                            <th>Duration</th><th>Status</th><th class="text-center">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty packages}">
                                <tr><td colspan="7" class="text-center text-muted py-4">No packages found.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="pkg" items="${packages}" varStatus="s">
                                    <tr>
                                        <td class="text-muted">${s.count}</td>
                                        <td class="fw-semibold"><c:out value="${pkg.name}"/></td>
                                        <td>
                                            <span class="badge ${pkg.packageType == 'PHOTO' ? 'bg-primary' : 'bg-danger'}">
                                                <i class="bi bi-${pkg.packageType == 'PHOTO' ? 'camera' : 'camera-video'} me-1"></i>${pkg.packageType}
                                            </span>
                                        </td>
                                        <td>Rs. <c:out value="${pkg.price}"/></td>
                                        <td>${pkg.durationHours} hrs</td>
                                        <td>
                                            <span class="badge ${pkg.available ? 'bg-success' : 'bg-secondary'}">
                                                ${pkg.available ? 'Available' : 'Unavailable'}
                                            </span>
                                        </td>
                                        <td class="text-center">
                                            <a href="/admin/packages/edit/${pkg.id}" class="btn btn-sm btn-outline-primary me-1">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <form action="/admin/packages/delete/${pkg.id}" method="post" class="d-inline"
                                                  onsubmit="return confirm('Delete package?')">
                                                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<footer class="bg-dark text-white-50 text-center py-3 mt-5">
    <small>&copy; 2026 LensStudio &mdash; OOP Project | Spring Boot + JSP</small>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

