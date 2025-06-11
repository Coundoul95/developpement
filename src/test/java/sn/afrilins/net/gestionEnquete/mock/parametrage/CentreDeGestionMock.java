package sn.afrilins.net.gestionEnquete.mock.parametrage;

import sn.afrilins.net.gestionEnquete.domain.parametrage.CentreDeGestion;

public class CentreDeGestionMock {

    public static CentreDeGestion getCentreDeGestion(){
        return CentreDeGestion.builder().code("CG0001").build();
    }
}
