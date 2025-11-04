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
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;
import sn.afrilins.net.gestionEnquete.domain.enume.TypeConcerne;

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
@Table(name = "DEMANDE_CONCERNE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Concerne {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEMANDE_CONCERNE")
    @SequenceGenerator(name = "SEQ_DEMANDE_CONCERNE", sequenceName = "SEQ_DEMANDE_CONCERNE", allocationSize = 1)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    TypeConcerne type;

    @Column(name = "telephone", unique = true, nullable = false)
    String telephone;

    @Column(name = "region_social", nullable = false)
    String regionSocial;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "concerne", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("concerne")
    List<DemandeEnquete> demandeEnquetes = new ArrayList<>();
}
