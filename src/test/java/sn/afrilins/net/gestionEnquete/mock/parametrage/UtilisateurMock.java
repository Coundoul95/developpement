package sn.afrilins.net.gestionEnquete.mock.parametrage;

import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;

public class UtilisateurMock {
    public static Utilisateur getUtilisateur(){
        return Utilisateur.builder().username("admin@afrilins").build();
    }
}
