package com.example.price_comparison.model;

public enum Role {
    USER(false),
    ADMIN(true);

    private final boolean isAdmin;

    Role(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No role found for: " + role);
    }
    
    @Override
    public String toString() {
        return name() + " (isAdmin: " + isAdmin + ")";
    }
}
