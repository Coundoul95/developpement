package sn.afrilins.net.gestionEnquete.domain.enume;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.text.MessageFormat;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Etat {

    MEUBLE("MEUBLE"),
    VIDE("VIDE");

    // @JsonValue allow to print this property on serialisation POJO -> JSON
    @JsonValue
    final String value;

    Etat(String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Etat fromValue(Object value) {
        for (Etat etat : values()) {
            if (etat.value.equals(value)) {

                return etat;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", Etat.class, value, values()));
    }
}
