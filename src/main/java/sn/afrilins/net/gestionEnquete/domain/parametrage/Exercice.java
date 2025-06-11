package sn.afrilins.net.gestionEnquete.domain.parametrage;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners({AuditingEntityListener.class})
@Table(name = "PARAMETRAGE_EXERCICE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exercice {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PARAMETRAGE_EXERCICE")
    @SequenceGenerator(name = "SEQ_PARAMETRAGE_EXERCICE", sequenceName = "SEQ_PARAMETRAGE_EXERCICE", allocationSize = 1)
    Long id;

    @Column(name = "annee", nullable = false, unique = true)
    String annee;

}
