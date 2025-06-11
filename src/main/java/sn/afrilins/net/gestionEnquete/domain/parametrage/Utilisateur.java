package sn.afrilins.net.gestionEnquete.domain.parametrage;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "PARAMETRAGE_UTILISATEUR")
@Builder
public class Utilisateur {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PARAMETRAGE_UTILISATEUR")
    @SequenceGenerator(name = "SEQ_PARAMETRAGE_UTILISATEUR", sequenceName = "SEQ_PARAMETRAGE_UTILISATEUR", allocationSize = 1)
    Long id;

    @Column(name = "username", unique = true)
    String username;
}

