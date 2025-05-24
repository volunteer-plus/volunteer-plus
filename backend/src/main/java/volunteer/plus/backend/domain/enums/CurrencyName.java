package volunteer.plus.backend.domain.enums;

import lombok.Getter;

@Getter
public enum CurrencyName {
    EUR("EUR", 978),
    UAH("UAH", 980),
    USD("USD", 840);

    private final String code;        // ISO 4217 alphabetic code

    private final int numericCode;    // ISO 4217 numeric code

    CurrencyName(String code, int numericCode) {
        this.code = code;
        this.numericCode = numericCode;
    }

}

