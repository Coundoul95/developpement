package sn.afrilins.net.gestionEnquete.services.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.afrilins.net.gestionEnquete.config.TokenRelayRequestInterceptor;
import sn.afrilins.net.gestionEnquete.services.remote.dto.CreationSansDossierDTO;
import sn.afrilins.net.gestionEnquete.services.remote.dto.RetourCreationSansDossierDTO;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient(name = "frontoffice", configuration = TokenRelayRequestInterceptor.class)
public interface FrontOfficeGlobalService {

    @RequestMapping(value = "/v1/api/transferts/action", method = PUT)
    ResponseEntity<?> updateTransfert(@RequestParam Long idTransfert, @RequestParam String fonction, @RequestHeader(value = "X-Tenant-ID", required = true) String tenant);

    @RequestMapping(value = "/v1/api/dossiers/sansDossier", method=POST)
    ResponseEntity<RetourCreationSansDossierDTO> creerDossier(@RequestBody CreationSansDossierDTO creationSansDossierDTO);


    @RequestMapping(value = "/v1/api/transferts/cloturer/{id}", method = GET)
    ResponseEntity<?> clotureDossier(@PathVariable Long id, @RequestHeader(value = "X-Tenant-ID", required = true) String tenant);


    @RequestMapping(value = "/v1/api/transferts/annulation", method = GET)
    ResponseEntity<?> annulationAction(@RequestParam(value = "idTransfert",required = false) Long id, @RequestParam(value = "numeroDossier",required = false) String numeroDossier, @RequestHeader(value = "X-Tenant-ID", required = true) String tenant);

}
