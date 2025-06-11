package sn.afrilins.net.gestionEnquete.domain.demande;

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
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners({AuditingEntityListener.class})
@Table(name = "DEMANDE_DEMANDE_ENQUETE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeEnquete extends AbstractAuditingEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEMANDE_DEMANDE_ENQUETE")
    @SequenceGenerator(name = "SEQ_DEMANDE_DEMANDE_ENQUETE", sequenceName = "SEQ_DEMANDE_DEMANDE_ENQUETE", allocationSize = 1)
    Long id;
}
