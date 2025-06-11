package sn.afrilins.net.gestionEnquete.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sn.afrilins.net.gestionEnquete.common.error.ErrorStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Builder
public class GestionEnqueteException extends RuntimeException{

    ErrorStatus status;

    String code;
}
