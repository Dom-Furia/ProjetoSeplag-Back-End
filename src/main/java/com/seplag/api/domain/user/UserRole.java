package com.seplag.api.domain.user;

public enum UserRole {
    ADMINISTRADOR("admin"),
    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
