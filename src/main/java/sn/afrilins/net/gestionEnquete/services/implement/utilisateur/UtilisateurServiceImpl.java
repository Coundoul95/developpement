package sn.afrilins.net.gestionEnquete.services.implement.utilisateur;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.repository.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.services.interfaces.utilisateur.UtilisateurService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Utilisateur getCurrentUser(String username) {

        Optional<Utilisateur> utilisateur = utilisateurRepository.findByUsername(username);
        return utilisateur.orElseGet(() -> utilisateurRepository.save(Utilisateur.builder().username(username).build()));
    }
}
