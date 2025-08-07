package sn.afrilins.net.gestionEnquete.domain.demande;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sn.afrilins.net.gestionEnquete.domain.audit.AbstractAuditingEntity;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.util.ReferenceGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@EntityListeners({AuditingEntityListener.class})
@Table(name = "DEMANDE_DEMANDE_ENQUETE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeEnquete  {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEMANDE_DEMANDE_ENQUETE")
    @SequenceGenerator(name = "SEQ_DEMANDE_DEMANDE_ENQUETE", sequenceName = "SEQ_DEMANDE_DEMANDE_ENQUETE", allocationSize = 1)
    Long id;

    @Column(name = "objet", nullable = false)
    String objet;

    @Column(name = "description")
    String description;

    @Column(name = "urgent")
    Boolean urgent;

    @Column(name = "commentaire_validation")
    String commentaireValidation;

    @Column(name = "priorite")
    @Builder.Default
    int priorite = 3;

    @Column(name = "date_validation")
    LocalDateTime dateValidation;

    @Column(name = "date_annulation")
    LocalDateTime dateAnnulation;

    @Column(name = "date_echeance")
    LocalDateTime dateEcheance;

    @Column(name = "reference", nullable = false, unique = true)
    String reference ;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concerne_id", nullable = false)
    @JsonIgnoreProperties("demandeEnquetes")
    Concerne concerne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etat_demande_id", nullable = false)
    @JsonIgnoreProperties("demandeEnquetes")
    EtatDemande etat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties("demandeEnquetes")
    Utilisateur utilisateur;


    @OneToMany(mappedBy = "demandeEnquete", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Document> documents = new ArrayList<>();

    @OneToOne(mappedBy = "demandeEnquete", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JsonIgnoreProperties("demandeEnquete")
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

    @PrePersist
    public void prePersist() {
        if (reference == null || reference.isEmpty()) {
            reference = ReferenceGenerator.generateReference("DEM");
        }
    }
}
