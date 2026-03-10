package com.safeflight.backend.model;

public enum DiscountType {
	NONE("No Discount", 0),
    SENIOR("Senior Citizen (10%)", 10),
    CHILD("Child (15%)", 15),
    MILITARY("Military Personnel (20%)", 20);

    private final String label;
    private final int percentage;

    DiscountType(String label, int percentage) {
        this.label = label;
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public int getPercentage() {
        return percentage;
    }
}
