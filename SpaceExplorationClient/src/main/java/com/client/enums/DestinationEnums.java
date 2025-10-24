package com.client.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationEnums {

    @Getter
    public enum EntityType {
        PLANET("Planet"),
        STAR("Star"),
        ASTEROID("Asteroid");
        private final String displayName;
        EntityType(String displayName) {
            this.displayName = displayName;
        }
    }

}
