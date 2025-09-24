package com.client.enums;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AstronautEnums {
    public enum HealthStatus {
        FLIGHT_READY("Flight Ready"),
        MEDICAL_REVIEW("Medical Review"),
        RETIRED("Retired");

        private final String displayName;

        HealthStatus(String displayName) {
            this.displayName = displayName;
        }

    }
    public enum Specialization {
        PILOT("Pilot"),
        ENGINEER("Engineer"),
        SCIENTIST("Scientist"),
        DOCTOR("Doctor"),
        GEOLOGIST("Geologist");

        private final String displayName;

        Specialization(String displayName) {
            this.displayName = displayName;
        }

    }
}
