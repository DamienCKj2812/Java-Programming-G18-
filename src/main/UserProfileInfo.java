package main;

public class UserProfileInfo {
    private static String username;
    private static String role;

    // Setter methods
    public static void setUsername(String username) {
        UserProfileInfo.username = username;
    }

    public static void setRole(String role) {
        UserProfileInfo.role = role;
    }

    // Getter methods
    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }
}

