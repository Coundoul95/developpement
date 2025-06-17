package sn.afrilins.net.gestionEnquete.config;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import sn.afrilins.net.gestionEnquete.domain.parametrage.Utilisateur;
import sn.afrilins.net.gestionEnquete.repository.UtilisateurRepository;
import sn.afrilins.net.gestionEnquete.util.InfoUserService;

import java.util.Objects;
import java.util.Optional;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Component
@AllArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Utilisateur> {


    private final InfoUserService infoUserService;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Optional<Utilisateur> getCurrentAuditor() {
        System.out.println("transaction========222tttttt22========" + TransactionSynchronizationManager.getCurrentTransactionName());

        if (Objects.nonNull(infoUserService.getCurrentUsername()) && infoUserService.getCurrentUsername().isPresent()) {
            String username = infoUserService.getCurrentUsername().get();

            var utilisateur = utilisateurRepository.findByUsername(username);
            if (utilisateur.isEmpty()) {
                Utilisateur utilisateursave = utilisateurRepository.save(Utilisateur.builder().username(infoUserService.getCurrentUsername().get()).build());
                if(Objects.nonNull(utilisateursave)) {
                    utilisateur = Optional.of(utilisateursave);
                }

            }
            return utilisateur;
        } else {
            String username = "kafka";

            var utilisateur = utilisateurRepository.findByUsername(username);
            if (utilisateur.isEmpty()) {
                Utilisateur utilisateursave = utilisateurRepository.save(Utilisateur.builder().username(username).build());
                if(Objects.nonNull(utilisateursave)) {
                    utilisateur = Optional.of(utilisateursave);
                }
                utilisateur = Optional.empty();

            }
            return utilisateur;
        }
    }

}

