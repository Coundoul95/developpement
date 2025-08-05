package sn.afrilins.net.gestionEnquete.domain.enume;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.MessageFormat;

public enum TypeConcerne {
    EMPLOYEUR("employeur"),
    BENEFICIAIRE("bénéficiaire"),
    TRAVAILLEUR("travailleur");

    @JsonValue
    final String value;

    TypeConcerne(String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeConcerne fromValue(Object value) {
        for (TypeConcerne type : values()) {
            if (type.value.equalsIgnoreCase(String.valueOf(value))) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                MessageFormat.format("{0} not found with the value: {1} in [{2}]",
                        TypeConcerne.class.getSimpleName(),
                        value,
                        java.util.Arrays.toString(values()))
        );
    }

    @Override
    public String toString() {
        return value;
    }
}
