package sn.afrilins.net.gestionEnquete.domain.parametrage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;

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
@Table(name = "PARAMETRAGE_DOCUMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Document {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_PARAMETRAGE_DOCUMENT")
    @SequenceGenerator(name = "SEQ_GE_PARAMETRAGE_DOCUMENT", sequenceName = "SEQ_GE_PARAMETRAGE_DOCUMENT", allocationSize = 1)
    Long id;

    @Column(name = "nom", nullable = false)
    String nom;

    @Column(name = "description")
    String description;

    @Column(name = "chemin",  nullable = false)
    String chemin;

    @Column(name = "extension",  nullable = false)
    String extension;

    @Column(name = "taille", nullable = false)
    int taille;

    @Column(name = "version", nullable = false)
    @Builder.Default
    int version = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "type_document_id", nullable = false)
    @JsonIgnoreProperties({"type_document"})
    TypeDocument type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_info_id") // champ nullable → une source_info est facultative
    @JsonIgnoreProperties("documents")
    SourceInfo sourceInfo;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "utilisateur_id", nullable = false)
//    Utilisateur utilisateur;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
