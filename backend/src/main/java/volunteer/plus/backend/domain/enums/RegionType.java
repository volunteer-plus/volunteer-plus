package volunteer.plus.backend.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegionType {
    STATE("State"),
    DISTRICT("District"),
    COMMUNITY("Community"),
    CITY_OR_VILLAGE("CityOrVillage"),
    CITY_DISTRICT("CityDistrict"),
    NULL("Null");

    private final String value;

    @JsonCreator
    public static RegionType fromValue(final String v) {
        for (final RegionType t : values()) {
            if (t.value.equalsIgnoreCase(v)) return t;
        }
        throw new IllegalArgumentException("Unknown V2RegionType: " + v);
    }
}
