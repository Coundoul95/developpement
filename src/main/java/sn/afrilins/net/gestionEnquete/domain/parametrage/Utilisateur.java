package sn.afrilins.net.gestionEnquete.domain.parametrage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sn.afrilins.net.gestionEnquete.domain.enquete.SourceInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "PARAMETRAGE_UTILISATEUR")
@Builder
public class Utilisateur {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GE_PARAMETRAGE_UTILISATEUR")
    @SequenceGenerator(name = "SEQ__GE_PARAMETRAGE_UTILISATEUR", sequenceName = "SEQ_GE_PARAMETRAGE_UTILISATEUR", allocationSize = 1)
    Long id;

    @Column(name = "username", unique = true)
    String username;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("utilisateur")
    List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("utilisateur")
    List<SourceInfo> sourceInfos = new ArrayList<>();

}

