package sn.afrilins.net.gestionEnquete.services.dto.parametrage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DocumentDebugInfo", description = "Modèle contenant des informations de diagnostic sur le fichier d’un document (chemin, taille, existence, etc.).")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentDebugInfo {

    @Schema(description = "Identifiant unique du document")
    Long documentId;

    @Schema(description = "Nom du document tel qu'enregistré en base")
    String nomDocument;

    @Schema(description = "Chemin relatif du fichier tel qu'enregistré (ex: /documents/fichier.pdf)")
    String cheminStocke;

    @Schema(description = "Chemin absolu ou résolu utilisé pour accéder physiquement au fichier")
    String cheminResolu;

    @Schema(description = "Indique si le fichier existe réellement à l'emplacement attendu")
    boolean fichierExiste;

    @Schema(description = "Indique si le fichier est lisible (permissions suffisantes)")
    boolean fichierLisible;

    @Schema(description = "Taille enregistrée du fichier (en Ko ou selon votre unité)")
    Integer tailleStockee;

    @Schema(description = "Taille réelle du fichier sur le disque (en octets)")
    Long tailleReelle;

    @Schema(description = "Répertoire racine ou base utilisé pour la résolution des chemins (ex: classpath:static)")
    String repertoireBase;
}
