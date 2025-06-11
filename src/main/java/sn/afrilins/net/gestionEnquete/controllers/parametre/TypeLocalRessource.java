package sn.afrilins.net.gestionEnquete.controllers.parametre;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeLocalDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeLocalService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/type/local")
@Tag(name = "/v1/api/type/local", description = "typeLocal, controllers")
@RequiredArgsConstructor
@Slf4j
public class TypeLocalRessource {

    private final TypeLocalService typeLocalService;

    @Operation(summary = "Creation type Local", description = "Il prend en entre un type Local")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrecte"),
            @ApiResponse(responseCode = "500", description = "Problèment du serveur")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeLocalDTO createTypeLocal(@Valid @RequestBody TypeLocalDTO typeLocal) {

        typeLocal.setId(null);

        return typeLocalService.createTypeLocal(typeLocal);
    }

    @Operation(summary = "Modification type Local", description = "il prend en entre un type local")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réussie"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrect"),
            @ApiResponse(responseCode = "404", description = "La ressource est innacessible"),
            @ApiResponse(responseCode = "401", description = "Ereur d'accès , Veuillez vérifiez votre authentification"),
            @ApiResponse(responseCode = "500", description = "Probléme du serveur")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeLocalDTO updateTypeLocal(@Parameter(description = "Identifiant type local", name = "id", required = true) @PathVariable(value = "id") Long id, @RequestBody @Valid TypeLocalDTO typeLocal) {
        typeLocal.setId(id);
        return typeLocalService.updateTypeLocal(typeLocal);
    }

    @Operation(summary = "Liste des type Local", description = "Permet de lister les type Locale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeLocalDTO> readTypeLocal(Pageable pageable
            , @Parameter(description = "le code du typeLocal") @RequestParam(value = "code", required = false) String code
            , @Parameter(description = "le libelle du typeLocal") @RequestParam(value = "libelle", required = false) String libelle
    ) {
        return typeLocalService.readAllTypeLocal(pageable, code, libelle);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "l'expression de besoin est bien trouve"),
            @ApiResponse(responseCode = "500", description = "le serveur ne fonctionne pas correctement"),
            @ApiResponse(responseCode = "403", description = "probleme accees")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    TypeLocalDTO findTypeLocalById(@PathVariable(name = "id") Long id) {
        return typeLocalService.findTypeLocalById(id);
    }


    @Operation(summary = "Suppression d'un typeLocal", description = "Suppression d'un typeLocal, Il prend en entre un identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aucun contenu"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrect"),
            @ApiResponse(responseCode = "404", description = "La ressource n'existe pas"),
            @ApiResponse(responseCode = "500", description = "Problème du serveur")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTypeLocal(@Parameter(name = "id", description = "l'identifiant d'un typeLocal") @PathVariable Long id) {
        typeLocalService.deleteTypeLocal(id);
    }
}
