package sn.afrilins.net.gestionEnquete.domain.enquete;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Document;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;

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
@Table(name = "ENQUETE_SOURCE_INFO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SourceInfo {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_ENQUETE_SOURCE_INFO")
    @SequenceGenerator(name = "SEQ_GE_ENQUETE_SOURCE_INFO", sequenceName = "SEQ_GE_ENQUETE_SOURCE_INFO", allocationSize = 1)
    Long id;

    @Column(unique = true, nullable = false)
    String nom;

    @Lob
    @Column(name = "description")
    String description;

    @Lob
    @Column(name = "commentaires")
    String commentaires;

    @Column(name = "fiabilite", nullable = false)
    @Builder.Default
    int fiabilite = 3;

    @CreationTimestamp
    @Column(name = "date_obtention")
    @Builder.Default
    LocalDateTime dateObtention = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "date_mis_a_jour")
    @Builder.Default
    LocalDateTime dateMiseAJour = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etat_source_info_id", nullable = false)
    @JsonIgnoreProperties({"etat_source_info"})
    EtatSourceInfo etat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_source_id", nullable = false)
    @JsonIgnoreProperties({"type_source"})
    TypeSource type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"source_info"})
    Utilisateur utilisateur;

    @ManyToMany
    @JoinTable(
            name = "enquete_source_info_link",
            joinColumns = @JoinColumn(name = "source_info_id"),
            inverseJoinColumns = @JoinColumn(name = "enquete_id")
    )
    @JsonIgnoreProperties("sourcesInfo")
    @Builder.Default
    private List<Enquete> enquetes = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "source_document",
            joinColumns = @JoinColumn(name = "source_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    @JsonIgnoreProperties("sourceInfos")
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    public void addDocument(Document document) {
        this.documents.add(document);
        document.getSourceInfos().add(this); // mise à jour côté inverse
    }

    public void addEnquete(Enquete enquete) {
        this.enquetes.add(enquete);
        enquete.getSourcesInfos().add(this); // mise à jour côté inverse
    }

    public void removeDocument(Document document) {
        this.documents.remove(document);
        document.getSourceInfos().remove(this);
    }
}

