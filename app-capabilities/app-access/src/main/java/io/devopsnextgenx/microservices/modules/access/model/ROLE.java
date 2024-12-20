package io.devopsnextgenx.microservices.modules.access.model;

public enum ROLE {
    SYSTEM_ADMINISTRATOR("SYSTEM_ADMINISTRATOR"),
    ORGANIZATION_ADMINISTRATOR("ORGANIZATION_ADMINISTRATOR"),
    DATA_MANAGER("DATA_MANAGER"),
    COMPANY_ADMINISTRATOR("COMPANY_ADMINISTRATOR"),
    SERVICE_ACCOUNT("SERVICE_ACCOUNT");

    private String name;

    ROLE(String name) {
        this.name = name;
    }

    public String getRoleName() {
        return name;
    }
}
