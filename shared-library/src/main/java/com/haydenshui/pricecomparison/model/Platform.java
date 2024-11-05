package com.haydenshui.pricecomparison.shared.model;

public enum Platform {
    JD("jd"),
    TMALL("tmall"),
    TAOBAO("taobao"),
    VIP("vip"),
    ;
    
    private final String name;

    Platform(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public static Platform fromString(String platform) {
        for (Platform p : Platform.values()) {
            if (p.name.equalsIgnoreCase(platform)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid platform: " + platform);
    }

    public static boolean isValidPlatform(String platform) {
        try {
            fromString(platform);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String[] getAllPlatforms() {
        return new String[]{"jd", "tmall", "taobao", "vip", "dangdang"};
    }
}
