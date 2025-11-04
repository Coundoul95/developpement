package sn.afrilins.net.gestionEnquete.domain.parametrage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.afrilins.net.gestionEnquete.domain.demande.DemandeEnquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.Enquete;
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;

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
@Table(name = "PARAMETRAGE_DOCUMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_PARAMETRAGE_DOCUMENT")
    @SequenceGenerator(name = "SEQ_GE_PARAMETRAGE_DOCUMENT", sequenceName = "SEQ_GE_PARAMETRAGE_DOCUMENT", allocationSize = 1)
    Long id;

    @Column(nullable = false)
    String nom;

    @Column(columnDefinition = "CLOB")
    String description;

    @Column(nullable = false)
    String chemin;

    @Column(nullable = false)
    String extension;

    @Column(nullable = false)
    int taille;

    @Column(nullable = false)
    @Builder.Default
    int version = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_document_id", nullable = false)
    @JsonIgnoreProperties("documents")
    TypeDocument type;

    @ManyToMany
    @JoinTable(
            name = "demande_enquete_document",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "demande_enquete_id")
    )
    @JsonIgnoreProperties("documents")
    @Builder.Default
    List<DemandeEnquete> demandesEnquete = new ArrayList<>();

    @ManyToMany(mappedBy = "documents")
    @JsonIgnoreProperties("documents")
    @Builder.Default
    List<SourceInfo> sourceInfos = new ArrayList<>();

    @ManyToMany(mappedBy = "documents")
    @JsonIgnoreProperties("documents")
    @Builder.Default
    List<Enquete> enquetes = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"document"})
    Utilisateur utilisateur;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;
}
