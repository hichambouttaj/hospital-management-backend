package com.ghopital.projet.dto;

public enum ROLES {
    ROLE_ADMIN(1),
    ROLE_MANAGEMENT_EMPLOYEES(2),
    ROLE_MANAGEMENT_MATERIALS(3),
    ROLE_MANAGEMENT_PHARMACY(4),
    ROLE_MANAGEMENT_PRODUCT(5);
    private final int id;
    ROLES(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
