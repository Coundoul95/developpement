package sn.afrilins.net.gestionEnquete.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.MessageFormat;

public enum LibellePeriodicitePaiement {

    MENSUELLE("MENSUELLE"),
    BIMESTRIELLE("BIMESTRIELLE"),
    TRIMESTRIELLE("TRIMESTRIELLE"),
    QUADRIMESTRIELLE("QUADRIMESTRIELLE"),
    SEMESTRIELLE("SEMESTRIELLE"),
    ANNUELLE("ANNUELLE");

    @JsonValue
    final String value;

    LibellePeriodicitePaiement(String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LibellePeriodicitePaiement fromValue(Object value) {
        for (LibellePeriodicitePaiement libellePeriodicitePaiement : values()) {
            if (libellePeriodicitePaiement.value.equals(value)) {
                return libellePeriodicitePaiement;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", LibellePeriodicitePaiement.class, value, values()));
    }
}
