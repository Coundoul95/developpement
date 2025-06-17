package sn.afrilins.net.gestionEnquete.domain.parametrage;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sn.afrilins.net.gestionEnquete.domain.audit.AbstractAuditingEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners({AuditingEntityListener.class})
@Table(name = "PARAMETRAGE_CENTRE_DE_GESTION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CentreDeGestion  extends AbstractAuditingEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_PARAMETRAGE_CENTRE_DE_GESTION")
    @SequenceGenerator(name = "SEQ_GE_PARAMETRAGE_CENTRE_DE_GESTION", sequenceName = "SEQ_GE_PARAMETRAGE_CENTRE_DE_GESTION", allocationSize = 1)
    Long id;

    @Column(name = "code", nullable = false, unique = true)
    String code;

}
