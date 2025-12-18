package com.auth.wow.libre.domain.model.enums;

import lombok.*;

@Getter
public enum HotelLocations {
    HOTEL_DUROTAR(14D, 1297, 1237.9623, -4494.53, 24.295612, 3.9405098, true),
    HOTEL_ORGRIMMAR(1637D, 1637, 1532.9116, -4355.671, 29.704905, 1.4953812, true),
    HOTEL_ELWYNN(12D, 87, -9464.671, 32.778305, 64.59675, 6.033303, false),
    HOTEL_POSADA(12D, 87, -9472.801, 37.318245, 64.6919, 4.666859, false);

    private final Double zona;
    private final Integer area;
    private final Double x;
    private final Double y;
    private final Double z;
    private final Double orientation;
    private final boolean isHorde;

    HotelLocations(Double zona, Integer area, Double x, Double y, Double z, Double orientation, boolean isHorde) {
        this.zona = zona;
        this.area = area;
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = orientation;
        this.isHorde = isHorde;
    }

}
