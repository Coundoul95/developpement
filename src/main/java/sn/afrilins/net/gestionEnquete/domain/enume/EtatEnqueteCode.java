package sn.afrilins.net.gestionEnquete.domain.enume;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.text.MessageFormat;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum EtatEnqueteCode {

    EN_ATTENTE("00"),
    EN_COURS("01"),
    TERMINEE("02"),
    EN_VALIDATION("03"),
    VALIDEE("04"),
    EN_REVISION("05"),
    ANNULEE("06");

    // @JsonValue permet de sérialiser l'enum en JSON avec sa valeur
    @JsonValue
    final String value;

    EtatEnqueteCode(String value) {
        this.value = value;
    }

    // Méthode pour récupérer l'enum à partir de sa valeur
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EtatEnqueteCode fromValue(Object value) {
        for (EtatEnqueteCode code : values()) {
            if (code.value.equals(value)) {
                return code;
            }
        }
        throw new IllegalArgumentException(
                MessageFormat.format(
                        "{0} not found with the value: {1} in [{2}]",
                        EtatEnqueteCode.class, value, values()
                )
        );
    }
}
