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
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.util.ReferenceGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@NamedEntityGraph(
        name = "Enquete.demandeEnquete",
        attributeNodes = @NamedAttributeNode("demandeEnquete")
)
public class Enquete {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_ENQUETE_ENQUETE")
    @SequenceGenerator(name = "SEQ_GE_ENQUETE_ENQUETE", sequenceName = "SEQ_GE_ENQUETE_ENQUETE", allocationSize = 1)
    Long id;

    @Column(name = "date_debut")
    LocalDateTime dateDebut;

    @Column(name = "date_fin")
    LocalDateTime dateFin;

    @Column(name = "date_validation")
    LocalDateTime dateValidation;

    @Column(name = "date_annulation")
    LocalDateTime dateAnnulation;

    @Column(name = "date_limite")
    LocalDateTime dateLimite;

    @Column(name = "date_assignation")
    LocalDateTime dateAssignation;

    @Lob
    @Column(name = "instruction")
    String instruction;

    @Column(name = "progression", nullable = false)
    @Builder.Default
    int progression = 0;

    @Column(name = "reference", nullable = false, unique = true)
    String reference ;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etat_enquete_id", nullable = false)
    @JsonIgnoreProperties("enquetes")
    EtatEnquete etat;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "demande_enquete_id", nullable = false, unique = true)
    @JsonIgnoreProperties("enquete")
    DemandeEnquete demandeEnquete;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "enqueteur_id", nullable = true)
    @JsonIgnoreProperties("enquetesAssignees")
    Utilisateur enqueteur;

    @ManyToMany(mappedBy = "enquetes")
    @JsonIgnoreProperties("enquetes")
    @Builder.Default
    List<SourceInfo> sourcesInfos = new ArrayList<>();

    @OneToMany(mappedBy = "enquete", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties("enquete")
    List<AutreInfo> autresInfos = new ArrayList<>();

    @OneToMany(mappedBy = "enquete", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnoreProperties("enquete")
    List<Conclusion> conclusions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "enquete_document",
            joinColumns = @JoinColumn(name = "enquete_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    @JsonIgnoreProperties("enquetes")
    @Builder.Default
    Set<Document> documents = new HashSet<>();


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (reference == null || reference.isEmpty()) {
            reference = ReferenceGenerator.generateReference("ENQ");
        }
    }

    public void addDocument(Document document) {
        this.documents.add(document);
        document.getEnquetes().add(this); // mise à jour côté inverse
    }

}
