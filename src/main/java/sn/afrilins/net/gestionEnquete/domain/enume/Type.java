package sn.afrilins.net.gestionEnquete.domain.enume;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.text.MessageFormat;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Type {

    VILLA("VILLA"),
    IMMEUBLE("IMMEUBLE"),
    APPARTEMENT("APPARTEMENT"),
    TERRAIN("TERRAIN"),
    MAGASIN("MAGASIN");
    // @JsonValue allow to print this property on serialisation POJO -> JSON
    @JsonValue
    final String value;

    Type(String value) {
        this.value = value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Type fromValue(Object value) {
        for (Type type : values()) {
            if (type.value.equals(value)) {

                return type;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", sn.afrilins.net.gestionEnquete.domain.enume.Type.class, value, values()));
    }
}
