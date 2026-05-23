package net.fernandosalas.ems.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PetCategory {
    CAT("cat"),
    DOG("dog");

    private final String value;

    PetCategory(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PetCategory fromValue(String value) {
        for (PetCategory category : values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid pet category: " + value + ". Allowed values: cat, dog");
    }
}
