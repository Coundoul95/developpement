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
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners({AuditingEntityListener.class})
@Table(name = "PARAMETRAGE_TYPE_LOCAL")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeLocal extends AbstractAuditingEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_PARAMETRAGE_TYPE_LOCAL")
    @SequenceGenerator(name = "SEQ_GE_PARAMETRAGE_TYPE_LOCAL", sequenceName = "SEQ_GE_PARAMETRAGE_TYPE_LOCAL", allocationSize = 1)
    Long id;

    @Column(name = "code")
    String code;

    @Column(name = "libelle")
    String libelle;
}
