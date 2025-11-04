package sn.afrilins.net.gestionEnquete.services.dto.demande.demande_enquete.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DemandeSerie", description = "Le modèle DemandeSerie")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemandeSerieDTO {

    @Schema(description = "Date du jour (yyyy-MM-dd)")
    String jour;

    @Schema(description = "Nombre de demandes créées")
    long creees;

    @Schema(description = "Nombre de demandes traitées")
    long traitees;

    /**
     * Transforme les résultats SQL en DTO et complète tous les jours de la semaine.
     * @param creees Liste des résultats des demandes créées
     * @param traitees Liste des résultats des demandes traitées
     * @param referenceDate Date de référence pour calculer la semaine (si null, prend aujourd'hui)
     * @return Liste de DemandeSerieDTO pour les 7 jours de la semaine
     */
    public static List<DemandeSerieDTO> fromResults(List<Object[]> creees,
                                                    List<Object[]> traitees,
                                                    LocalDate referenceDate) {
        Map<String, DemandeSerieDTO> map = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Transformer les demandes créées en map
        for (Object[] row : creees) {
            LocalDate date = ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate();
            String jour = date.format(formatter);
            Number count = (Number) row[1];
            map.put(jour, new DemandeSerieDTO(jour, count.longValue(), 0));
        }

        // Transformer les demandes traitées en map
        for (Object[] row : traitees) {
            LocalDate date = ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate();
            String jour = date.format(formatter);
            Number count = (Number) row[1];
            map.computeIfAbsent(jour, j -> new DemandeSerieDTO(j, 0, 0))
                    .setTraitees(count.longValue());
        }

        // Déterminer la semaine à partir de la date de référence
        LocalDate targetDate = (referenceDate != null) ? referenceDate : LocalDate.now();
        LocalDate startOfWeek = targetDate.minusDays(targetDate.getDayOfWeek().getValue() - 1); // lundi

        List<DemandeSerieDTO> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate jour = startOfWeek.plusDays(i);
            String key = jour.format(formatter);
            result.add(map.getOrDefault(key, new DemandeSerieDTO(key, 0, 0)));
        }

        return result;
    }
}
