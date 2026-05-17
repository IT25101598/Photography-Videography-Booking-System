<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Add Package – LensStudio</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>
    <style> body { background-color: #f8f9fa; } .card { border-top: 4px solid #198754; } </style>
</head>
<body>
<%@ include file="_navbar.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-7">
            <h2 class="fw-bold mb-4"><i class="bi bi-plus-circle text-success me-2"></i>Add New Package</h2>

            <c:if test="${not empty errorMsg}">
                <div class="alert alert-danger"><i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMsg}</div>
            </c:if>

            <div class="card shadow">
                <div class="card-body p-4">
                    <form action="/admin/packages/add" method="post" id="pkgForm" novalidate>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Package Name <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="name"
                                   placeholder="e.g. Wedding Platinum" required maxlength="100"/>
                            <div class="invalid-feedback">Package name is required (max 100 chars).</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Type <span class="text-danger">*</span></label>
                            <select class="form-select" name="type" required>
                                <option value="">— Select Type —</option>
                                <option value="PHOTO">Photography</option>
                                <option value="VIDEO">Videography</option>
                            </select>
                            <div class="invalid-feedback">Please select a package type.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Description <span class="text-danger">*</span></label>
                            <textarea class="form-control" name="description" rows="3"
                                      placeholder="Describe what is included (10–500 chars)..." required minlength="10" maxlength="500"></textarea>
                            <div class="invalid-feedback">Description must be 10–500 characters.</div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold">Price (Rs.) <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" name="price"
                                       placeholder="e.g. 50000" min="1" max="10000000" step="0.01" required/>
                                <div class="invalid-feedback">Price must be greater than zero.</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-semibold">Duration (hours) <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" name="durationHours"
                                       placeholder="e.g. 8" min="1" max="72" required/>
                                <div class="invalid-feedback">Duration must be between 1 and 72 hours.</div>
                            </div>
                        </div>

                        <div class="d-flex gap-2 mt-2">
                            <button type="submit" class="btn btn-success">
                                <i class="bi bi-save me-2"></i>Save Package
                            </button>
                            <a href="/admin/packages" class="btn btn-outline-secondary">Cancel</a>
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

    const validators = {
        name:          () => { const e = document.querySelector('[name=name]');          return e.value.trim().length >= 2 && e.value.trim().length <= 100 ? (setValid(e),true) : (setInvalid(e),false); },
        type:          () => { const e = document.querySelector('[name=type]');          return e.value ? (setValid(e),true) : (setInvalid(e),false); },
        description:   () => { const e = document.querySelector('[name=description]');   const l = e.value.trim().length; return (l>=10&&l<=500) ? (setValid(e),true) : (setInvalid(e),false); },
        price:         () => { const e = document.querySelector('[name=price]');         return parseFloat(e.value)>0 ? (setValid(e),true) : (setInvalid(e),false); },
        durationHours: () => { const e = document.querySelector('[name=durationHours]'); const v=parseInt(e.value); return (v>=1&&v<=72) ? (setValid(e),true) : (setInvalid(e),false); }
    };

    // live feedback
    document.querySelector('[name=name]').addEventListener('blur',          validators.name);
    document.querySelector('[name=type]').addEventListener('change',        validators.type);
    document.querySelector('[name=description]').addEventListener('blur',   validators.description);
    document.querySelector('[name=price]').addEventListener('blur',         validators.price);
    document.querySelector('[name=durationHours]').addEventListener('blur', validators.durationHours);

    document.getElementById('pkgForm').addEventListener('submit', function(e) {
        const ok = Object.values(validators).map(fn => fn());
        if (ok.includes(false)) e.preventDefault();
    });
</script>
</body>
</html>

