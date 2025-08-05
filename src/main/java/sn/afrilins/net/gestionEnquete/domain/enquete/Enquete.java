package sn.afrilins.net.gestionEnquete.domain.enquete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.demande.EtatDemande;

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
@Table(name = "ENQUETE_ENQUETE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Enquete {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_ENQUETE_ENQUETE")
    @SequenceGenerator(name = "SEQ_GE_ENQUETE_ENQUETE", sequenceName = "SEQ_GE_ENQUETE_ENQUETE", allocationSize = 1)
    Long id;

    @Column(name = "created_at", updatable = false)
    LocalDateTime dateDebut;

    @Column(name = "updated_at")
    LocalDateTime dateFin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etat_enquete_id", nullable = false)
    @JsonIgnoreProperties("enquetes")
    EtatEnquete etat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "demande_enquete_id", nullable = false)
    @JsonIgnoreProperties("enquetes")
    DemandeEnquete demandeEnquete;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
