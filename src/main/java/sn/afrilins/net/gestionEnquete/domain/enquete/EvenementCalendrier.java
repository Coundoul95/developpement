package sn.afrilins.net.gestionEnquete.domain.enquete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//@EntityListeners({AuditingEntityListener.class})
@Table(name = "ENQUETE_EVENEMENT_CALENDRIER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EvenementCalendrier {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_ENQUETE_EVENEMENT_CALENDRIER")
    @SequenceGenerator(name = "SEQ_GE_ENQUETE_EVENEMENT_CALENDRIER", sequenceName = "SEQ_GE_ENQUETE_EVENEMENT_CALENDRIER", allocationSize = 1)
    Long id;

    @Column(name = "titre", unique = true)
    String titre;

    @Column(name = "date_evenement")
    LocalDate date;

    @Column(name = "heure")
    String heure;

    @Column(name = "duree")
    Integer duree;

    @Column(name = "priorite")
    String priorite;

    @Column(length = 1000, name = "description")
    String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "type_id")
    TypeEvenement type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"evenement_calendriers"})
    Utilisateur utilisateur;

    @Version
    @Column(name = "version")
    Long version;

}
