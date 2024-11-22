package com.haydenshui.pricecomparison.shared.model;

public enum AlertType {
    ALERT_ON_ALL_PRICE_CHANGE("alert_on_price_change"),
    ALERT_ON_HIGHER_PRICE_CHANGE("alert_on_higher_price_change"),
    ALERT_ON_LOWER_PRICE_CHANGE("alert_on_lower_price_change"),
    ALERT_ON_UPPER_BOUND_REACH("alert_on_upper_bound_reach"),
    ALERT_ON_LOWER_BOUND_REACH("alert_on_lower_bound_reach"),
    NO_ALERT("no_alert");

    private final String name;
    
    AlertType(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public static AlertType fromString(String alertType) {
        for (AlertType type : AlertType.values()) {
            if (type.getName().equalsIgnoreCase(alertType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No type found for: " + alertType);
    }

    public static boolean isValidAlertType(String type) {
        try {
            fromString(type);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String[] getAllAlertTypes() {
        return new String[] {ALERT_ON_ALL_PRICE_CHANGE.getName()};
    }

    @Override
    public String toString() {
        return new String("Alert type: " + name());
    }
}
