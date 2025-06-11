package sn.afrilins.net.gestionEnquete.mock.parametrage;

import sn.afrilins.net.gestionEnquete.domain.parametrage.TypeLocal;

public class TypeLocalMock {

    public static TypeLocal getTypeLocal(){
        return TypeLocal.builder().code("TL001").libelle("F1").build();
    }
}
