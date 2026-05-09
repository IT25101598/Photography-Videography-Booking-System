package com.reviewapp.model;

public class VerifiedReview extends Review {

    public VerifiedReview() {
        super();
    }

    public VerifiedReview(String id, String packageId, String authorName,
                          String content, int rating) {
        super(id, packageId, authorName, content, rating);
    }

    @Override
    public String getReviewType() {
        return "VERIFIED";
    }

    @Override
    public String getDisplayFormat(boolean isAdmin) {
        String stars = buildStars();
        String badge = isAdmin
            ? "<span class='badge bg-info ms-2'>Verified Review</span>"
            : "<span class='badge bg-success'><i class='bi bi-check-circle-fill me-1'></i>Verified</span>";

        return String.format(
            "<div class='card shadow-sm h-100'>" +
            "  <div class='card-header bg-primary text-white d-flex justify-content-between align-items-center'>" +
            "    <span><i class='bi bi-person-circle me-2'></i>%s</span>" +
            "    %s" +
            "  </div>" +
            "  <div class='card-body'>" +
            "    <div class='mb-2'>" +
            "      <span class='badge bg-primary'>%s</span>" +
            "    </div>" +
            "    <div class='text-warning mb-2' style='font-size:1.2rem;'>%s</div>" +
            "    <p class='card-text'>%s</p>" +
            "  </div>" +
            "</div>",
            getAuthorName(),
            badge,
            getPackageId(),
            stars,
            getContent()
        );
    }
}

