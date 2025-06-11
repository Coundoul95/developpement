package sn.afrilins.net.gestionImmeubleRapport.services.interfaces.utilisateur;

import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;

public interface UtilisateurService {
    Utilisateur getCurrentUser(String username);
}
