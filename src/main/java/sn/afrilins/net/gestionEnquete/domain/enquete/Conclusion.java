package sn.afrilins.net.gestionEnquete.domain.enquete;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@EntityListeners({AuditingEntityListener.class})
@Table(name = "ENQUETE_CONCLUSION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conclusion {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_ENQUETE_CONCLUSION")
    @SequenceGenerator(name = "SEQ_GE_ENQUETE_CONCLUSION", sequenceName = "SEQ_GE_ENQUETE_CONCLUSION", allocationSize = 1)
    Long id;

    @Column(name = "titre", nullable = false, columnDefinition = "CLOB")
    String titre;

    @Column(name = "contenu", nullable = false, columnDefinition = "CLOB")
    String contenu;

    @Column(name = "recommandation", columnDefinition = "CLOB")
    String recommandation;

    @Column(name = "mesures_suivi", columnDefinition = "CLOB")
    String mesuresSuivi;

    @Column(name = "date_validation")
    LocalDateTime dateValidation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enquete_id", nullable = false)
    Enquete enquete;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    Long version;
}
