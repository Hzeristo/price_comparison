package com.haydenshui.pricecomparison.shared.model;

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

    public static boolean isValidRole(String role) {
        try {
            fromString(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String[] getAllRoles() {
        return new String[]{USER.name(), ADMIN.name()};
    }
    
    @Override
    public String toString() {
        return name() + " (isAdmin: " + isAdmin + ")";
    }
}
