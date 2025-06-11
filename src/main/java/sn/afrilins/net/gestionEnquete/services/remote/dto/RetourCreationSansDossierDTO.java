package sn.afrilins.net.gestionEnquete.services.remote.dto;

public class RetourCreationSansDossierDTO {
    private String numeroDossier;

    private Integer tag;

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }
}