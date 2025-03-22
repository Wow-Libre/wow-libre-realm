package com.auth.wow.libre.domain.model.enums;

import lombok.*;

@Getter
public enum NavigationRoutes {
    HOME("/"),
    MAIN("main"),
    REGISTER("register"),
    LOGIN("login"),
    DASHBOARD("dashboard"),
    CONGRATS("congrats");

    private final String path;

    NavigationRoutes(String path) {
        this.path = path;
    }


}
