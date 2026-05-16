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
    <style> body { background-color: #f8f9fa; } .card { border-top: 4px solid #0d6efd; } </style>
</head>
<body>
<%@ include file="_navbar.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h2 class="fw-bold mb-4"><i class="bi bi-pencil-square text-primary me-2"></i>Edit Staff Member</h2>

            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger"><i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMsg}</div>
            </c:if>

            <div class="card shadow">
                <div class="card-body p-4">
                    <form action="/admin/staff/edit" method="post" id="editStaffForm" novalidate>
                        <input type="hidden" name="id" value="${staffMember.id}"/>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Full Name <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="name"
                                   value="<c:out value='${staffMember.name}'/>"
                                   required maxlength="80" pattern="[A-Za-z .]{2,80}"/>
                            <div class="invalid-feedback">Name is required (letters, spaces, dots only).</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Email <span class="text-danger">*</span></label>
                            <input type="email" class="form-control" name="email"
                                   value="<c:out value='${staffMember.email}'/>" required/>
                            <div class="invalid-feedback">A valid email is required.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Phone</label>
                            <input type="text" class="form-control" name="phone"
                                   value="<c:out value='${staffMember.phone}'/>"
                                   pattern="[0-9+\-() ]{7,15}"/>
                            <div class="invalid-feedback">Enter a valid phone number.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Role <span class="text-danger">*</span></label>
                            <select class="form-select" name="role" required>
                                <option value="PHOTOGRAPHER" ${staffMember.staffRole == 'PHOTOGRAPHER' ? 'selected' : ''}>Photographer</option>
                                <option value="VIDEOGRAPHER" ${staffMember.staffRole == 'VIDEOGRAPHER' ? 'selected' : ''}>Videographer</option>
                            </select>
                        </div>

                        <div class="mb-4">
                            <div class="form-check form-switch">
                                <input class="form-check-input" type="checkbox" name="available"
                                       id="availableCheck" value="true" ${staffMember.available ? 'checked' : ''}/>
                                <label class="form-check-label" for="availableCheck">Available for Assignments</label>
                            </div>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save me-2"></i>Save Changes
                            </button>
                            <a href="/staff" class="btn btn-outline-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<footer class="bg-dark text-white-50 text-center py-3 mt-5">
    <small>&copy; 2026 LensStudio &mdash; OOP Project | Spring Boot + JSP</small>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function setValid(el)   { el.classList.remove('is-invalid'); el.classList.add('is-valid'); }
    function setInvalid(el) { el.classList.add('is-invalid');    el.classList.remove('is-valid'); }
    function clearState(el) { el.classList.remove('is-invalid','is-valid'); }

    function validateName() {
        const el = document.querySelector('[name=name]');
        return /^[A-Za-z .]{2,80}$/.test(el.value.trim()) ? (setValid(el),true) : (setInvalid(el),false);
    }
    function validateEmail() {
        const el = document.querySelector('[name=email]');
        return /^[\w._%+\-]+@[\w.\-]+\.[a-zA-Z]{2,}$/.test(el.value.trim()) ? (setValid(el),true) : (setInvalid(el),false);
    }
    function validatePhone() {
        const el = document.querySelector('[name=phone]');
        if (!el.value.trim()) { clearState(el); return true; }
        return /^[0-9+\-() ]{7,15}$/.test(el.value.trim()) ? (setValid(el),true) : (setInvalid(el),false);
    }
    function validateRole() {
        const el = document.querySelector('[name=role]');
        return el.value ? (setValid(el),true) : (setInvalid(el),false);
    }

    document.querySelector('[name=name]').addEventListener('blur',   validateName);
    document.querySelector('[name=email]').addEventListener('blur',  validateEmail);
    document.querySelector('[name=phone]').addEventListener('blur',  validatePhone);
    document.querySelector('[name=role]').addEventListener('change', validateRole);

    document.getElementById('editStaffForm').addEventListener('submit', function(e) {
        const ok = [validateName(), validateEmail(), validatePhone(), validateRole()];
        if (ok.includes(false)) e.preventDefault();
    });
</script>
</body>
</html>

