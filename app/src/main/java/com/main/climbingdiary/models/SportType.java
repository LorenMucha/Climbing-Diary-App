package com.main.climbingdiary.models;

public enum SportType {
    KLETTERN, BOULDERN;

    public static SportType stringToSportType(String type) {
        return SportType.valueOf(type.toUpperCase());
    }

    public String typeToString() {
        return this.toString().toLowerCase();
    }

    public String getRouteName() {
        return this == KLETTERN ? "Routen" : "Boulder";
    }
}
