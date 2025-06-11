package sn.afrilins.net.gestionEnquete.domain.audit;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import sn.afrilins.net.gestionEnquete.domain.parametrage.CentreDeGestion;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Exercice;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class  AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @ManyToOne
    @JoinColumn(name = "utilisateur_creation")
    @CreatedBy
    Utilisateur utilisateurCreation;

    @CreatedDate
    @Column(name = "date_creation")
    LocalDateTime dateCreation;


    @ManyToOne
    @JoinColumn(name = "utilisateur_modification")
    @LastModifiedBy
    Utilisateur utilisateurModification;

    @LastModifiedDate
    @Column(name = "date_modification")
    LocalDateTime dateModification;

    @ManyToOne
    @JoinColumn(name = "centre_de_gestion")
    CentreDeGestion centre;

    @ManyToOne
    @JoinColumn(name = "exercice_id")
    Exercice exercice;


}
