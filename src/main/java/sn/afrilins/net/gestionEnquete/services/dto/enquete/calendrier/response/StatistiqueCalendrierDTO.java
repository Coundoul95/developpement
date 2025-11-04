package sn.afrilins.net.gestionEnquete.services.dto.enquete.calendrier.response;


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
@Schema(name = "type_source", description = "le modele type_source")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatistiqueCalendrierDTO {

    @Schema(name = "totalEvenements", description = "Le total événement")
    int totalEvenements;

    @Schema(name = "totalEnquetes", description = "Le total enquête")
    int totalEnquetes;

    @Schema(name = "totalEcheances", description = "Le total échéance")
    int totalEcheances;

    @Schema(name = "totalUrgents", description = "Le total urgent")
    int totalUrgents;

}
