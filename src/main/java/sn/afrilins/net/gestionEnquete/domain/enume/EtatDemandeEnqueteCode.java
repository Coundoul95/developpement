package sn.afrilins.net.gestionEnquete.domain.enume;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.text.MessageFormat;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum EtatDemandeEnqueteCode {

    EN_ATTENTE("00"),
    VALIDER("01"),
    REJETER("02"),
    EN_COMPLEMENT("03"),
    ANNULER("04");

    // @JsonValue permet de sérialiser l'enum en JSON avec sa valeur
    @JsonValue
    final String value;

    EtatDemandeEnqueteCode(String value) {
        this.value = value;
    }

    // Méthode pour récupérer l'enum à partir de sa valeur
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EtatDemandeEnqueteCode fromValue(Object value) {
        for (EtatDemandeEnqueteCode code : values()) {
            if (code.value.equals(value)) {
                return code;
            }
        }
        throw new IllegalArgumentException(
                MessageFormat.format(
                        "{0} not found with the value: {1} in [{2}]",
                        EtatDemandeEnqueteCode.class, value, values()
                )
        );
    }
}
