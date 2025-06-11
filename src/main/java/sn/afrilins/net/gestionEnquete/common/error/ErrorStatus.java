package sn.afrilins.net.gestionEnquete.common.error;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ErrorStatus {

    BAD_REQUEST("404"),

    UNAUTHORIZED("401"),

    FORBIDDEN("403"),

    FOUND("400"),

    NOT_FOUND("404"),

    ETAT_INVALID("303"),

    INTERNAL_SERVER_ERROR("500"),

    FOUND_STAT("405"),

    INVENTAIRE_INVALID("600"),

    INVALID_QUANTITE_FACTURE("700"),

    QUANTITE_INVALID("309");


    @JsonValue
    final String value;

    ErrorStatus(String value) {
        this.value = value;
    }

}
