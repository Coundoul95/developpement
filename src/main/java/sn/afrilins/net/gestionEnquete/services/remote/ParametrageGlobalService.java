package sn.afrilins.net.gestionEnquete.services.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import sn.afrilins.net.gestionEnquete.services.dto.parametrage.CentreDeGestionDTO;

@FeignClient(name = "parametrageGlobal")
public interface ParametrageGlobalService {
    @GetMapping(value = "/v1/api/centre-de-gestions/get-centre-by-code")
    ResponseEntity<CentreDeGestionDTO> readCentreDeGestion(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @RequestParam(value = "code") String code);

    //    @GetMapping(value = "/v1/api/organigrammes/bycode/{code}")
//    ResponseEntity<OrganigrammeDTO> readOrganigramme(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable(value = "code") String code);

}
