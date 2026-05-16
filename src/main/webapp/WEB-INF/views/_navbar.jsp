<%-- _navbar.jsp – Shared Navigation Bar included in all views --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand fw-bold" href="/dashboard">
            <i class="bi bi-camera2 text-warning me-1"></i>LensStudio
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/packages">
                        <i class="bi bi-box-seam me-1"></i>Packages
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/staff">
                        <i class="bi bi-people-fill me-1"></i>Our Team
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/viewReviews">
                        <i class="bi bi-star-fill me-1"></i>Reviews
                    </a>
                </li>
                <c:if test="${not empty sessionScope.loggedInUser}">
                    <li class="nav-item">
                        <a class="nav-link" href="/bookings/new">
                            <i class="bi bi-calendar-plus me-1"></i>Book Now
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/bookings/my">
                            <i class="bi bi-calendar-check me-1"></i>My Bookings
                        </a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.loggedInUser.role == 'ADMIN'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle text-warning" href="#"
                           data-bs-toggle="dropdown">
                            <i class="bi bi-shield-fill me-1"></i>Admin
                        </a>
                        <ul class="dropdown-menu dropdown-menu-dark">
                            <li><a class="dropdown-item" href="/dashboard">
                                <i class="bi bi-speedometer2 me-2"></i>Dashboard</a></li>
                            <li><a class="dropdown-item" href="/users">
                                <i class="bi bi-person-lines-fill me-2"></i>Users</a></li>
                            <li><a class="dropdown-item" href="/admin/packages">
                                <i class="bi bi-box-seam me-2"></i>Packages</a></li>
                            <li><a class="dropdown-item" href="/admin/bookings">
                                <i class="bi bi-journal-check me-2"></i>Bookings</a></li>
                            <li><a class="dropdown-item" href="/staff">
                                <i class="bi bi-people me-2"></i>Staff</a></li>
                            <li><a class="dropdown-item" href="/adminModeration">
                                <i class="bi bi-chat-square-text me-2"></i>Reviews</a></li>
                        </ul>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedInUser}">
                        <li class="nav-item">
                            <a class="nav-link" href="/profile">
                                <i class="bi bi-person-circle me-1"></i>${sessionScope.loggedInUser.username}
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-danger" href="/logout">
                                <i class="bi bi-box-arrow-right me-1"></i>Logout
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="/login">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Login
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/register">
                                <i class="bi bi-person-plus me-1"></i>Register
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

