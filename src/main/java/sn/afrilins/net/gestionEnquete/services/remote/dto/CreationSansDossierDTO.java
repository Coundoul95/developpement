package sn.afrilins.net.gestionEnquete.services.remote.dto;

import lombok.ToString;

@ToString
public class CreationSansDossierDTO {

    private String codeProcessus;

    private String codeCentre;

    private String numeroDroit;

    public String getNumeroDroit() {
        return numeroDroit;
    }

    public void setNumeroDroit(String numeroDroit) {
        this.numeroDroit = numeroDroit;
    }

    public String getCodeProcessus() {
        return codeProcessus;
    }

    public void setCodeProcessus(String codeProcessus) {
        this.codeProcessus = codeProcessus;
    }

    public String getCodeCentre() {
        return codeCentre;
    }

    public void setCodeCentre(String codeCentre) {
        this.codeCentre = codeCentre;
    }
}
