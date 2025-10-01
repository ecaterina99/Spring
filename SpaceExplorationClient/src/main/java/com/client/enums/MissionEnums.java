package com.client.enums;

import lombok.Getter;

public class MissionEnums {
   @Getter
    public enum DifficultyLevel {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high"),
        EXTREME("extreme");

        private final String displayName;

        DifficultyLevel(String displayName) {
            this.displayName = displayName;
        }
    }
}
