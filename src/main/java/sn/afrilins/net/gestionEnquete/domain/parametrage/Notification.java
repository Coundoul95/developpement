package sn.afrilins.net.gestionEnquete.domain.parametrage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@EntityListeners({AuditingEntityListener.class})
@Table(name = "PARAMETRAGE_NOTIFICATION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_PARAMETRAGE_NOTIFICATION")
    @SequenceGenerator(name = "SEQ_GE_PARAMETRAGE_NOTIFICATION", sequenceName = "SEQ_GE_PARAMETRAGE_NOTIFICATION", allocationSize = 1)
    Long id;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    String message;

    @Column(name = "date_envoi", nullable = false)
    @CreationTimestamp
    LocalDateTime dateEnvoi;

    @Column(name = "date_lu", nullable = false)
    @CreationTimestamp
    LocalDateTime dateLu;

    @Column(name = "lu", nullable = false)
    @Builder.Default
    Boolean lu = false;

    @Column(name = "type_notification", nullable = false, length = 50)
    String typeNotification;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnoreProperties({"notifications"})
    Utilisateur utilisateur;


}
