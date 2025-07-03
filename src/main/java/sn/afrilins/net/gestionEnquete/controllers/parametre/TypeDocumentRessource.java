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
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.TypeDocumentDTO;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.request.TypeDocumentRequestDTO;
import sn.afrilins.net.gestionEnquete.services.interfaces.parametrage.TypeDocumentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/type/document")
@Tag(name = "/v1/api/type/document", description = "typeDocument, controllers")
@RequiredArgsConstructor
@Slf4j
public class TypeDocumentRessource {

    private final TypeDocumentService typeDocumentService;

    @Operation(summary = "Creation type document", description = "Il prend en entre un type document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrecte"),
            @ApiResponse(responseCode = "500", description = "Problèment du serveur")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeDocumentDTO createTypeDocument(@Valid @RequestBody TypeDocumentRequestDTO typeDocument) {
        return typeDocumentService.createTypeDocument(typeDocument);
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
    public TypeDocumentDTO updateTypeDocument(
        @Parameter(description = "Identifiant type document", name = "id", required = true) 
        @PathVariable(value = "id") Long id, 
        @RequestBody @Valid TypeDocumentDTO typeDocument) {
        typeDocument.setId(id);
        return typeDocumentService.updateTypeDocument(typeDocument);
    }

    @Operation(summary = "Liste des type Local", description = "Permet de lister les types de document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal server error during request processing")})
    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public Page<TypeDocumentDTO> readTypeDocument(
            Pageable pageable,
            @Parameter(description = "Le code") @RequestParam(required = false) String code,
            @Parameter(description = "Le libelle") @RequestParam(required = false) String libelle) {
        return typeDocumentService.readAllTypeDocument(code, libelle, pageable);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "l'expression de besoin est bien trouve"),
            @ApiResponse(responseCode = "500", description = "le serveur ne fonctionne pas correctement"),
            @ApiResponse(responseCode = "403", description = "probleme accees")
    })
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    TypeDocumentDTO findTypeDocumentById(@PathVariable(name = "id") Long id) {
        return typeDocumentService.findTypeDocumentById(id);
    }


    @Operation(summary = "Suppression d'un TypeDocument", description = "Suppression d'un TypeDocument, Il prend en entre un identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aucun contenu"),
            @ApiResponse(responseCode = "400", description = "La requête est syntaxiquement incorrect"),
            @ApiResponse(responseCode = "404", description = "La ressource n'existe pas"),
            @ApiResponse(responseCode = "500", description = "Problème du serveur")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTypeDocument(@Parameter(name = "id", description = "l'identifiant d'un TypeDocument") @PathVariable Long id) {
        typeDocumentService.deleteTypeDocument(id);
    }
}
