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
@Table(name = "ENQUETE_AUTRE_INFO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AutreInfo {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_ENQUETE_AUTRE_INFO")
    @SequenceGenerator(name = "SEQ_GE_ENQUETE_AUTRE_INFO", sequenceName = "SEQ_GE_ENQUETE_AUTRE_INFO", allocationSize = 1)
    Long id;

    @Column(name = "categorie", nullable = false)
    String categorie;

    @Column(name = "description", columnDefinition = "CLOB")
    String description;

    @Column(name = "importance", nullable = false)
    String importance;

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
