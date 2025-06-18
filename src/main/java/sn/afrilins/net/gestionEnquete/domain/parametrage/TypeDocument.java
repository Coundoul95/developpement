package sn.afrilins.net.gestionEnquete.domain.parametrage;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@Table(name = "PARAMETRAGE_TYPE_DOCUMENT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeDocument {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_PARAMETRAGE_TYPE_DOCUMENT")
    @SequenceGenerator(name = "SEQ_GE_PARAMETRAGE_TYPE_DOCUMENT", sequenceName = "SEQ_GE_PARAMETRAGE_TYPE_DOCUMENT", allocationSize = 1)
    Long id;

    @Column(name = "libelle", unique = true, nullable = false)
    String libelle;

    @Column(name = "code", unique = true, nullable = false)
    String code;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
