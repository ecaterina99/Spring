package com.client.enums;

import lombok.Getter;
@Getter
public class MissionSpecializationsEnums {

    @Getter
    public enum Specialization {
        PILOT("Pilot"),
        DOCTOR("Doctor"),
        SCIENTIST("Scientist"),
        GEOLOGIST("Geologist"),
        ENGINEER("Engineer");

        private final String displayName;

        Specialization(String displayName) {
            this.displayName = displayName;
        }
    }
}
