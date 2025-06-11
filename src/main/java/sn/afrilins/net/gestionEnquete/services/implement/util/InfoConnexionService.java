package sn.afrilins.net.gestionEnquete.services.implement.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class InfoConnexionService {
    private final HttpServletRequest request;
    @Value("${info.version}")
    String infoVersion;
    public InfoConnexionService(HttpServletRequest request) {
        this.request = request;
    }

    public String getCurrentCentre(){
        return request.getHeader("X-Tenant-ID");
    }
    public String getCurrentExercice(){
        if(infoVersion!=null && infoVersion.equalsIgnoreCase("test")){
            return "2022";
        }
        return request.getHeader("X-Exo-ID");
    }
}
