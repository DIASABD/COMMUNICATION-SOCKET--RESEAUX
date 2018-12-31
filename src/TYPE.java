

/**********************************************************************************
 *  Ce programme  permet de lire un fichier et de convertir les donner en bits et
 *  de les envoyer a un recepteur  avec lequel la connexion a ete etablie.        *
 * Il a été fait par Diasso Abdramane  matricule 20057513 et Willy Foadjo Mlle    *
 * 20059876 .  Il s'agit du dévoir 2 du cours IFT 3325 Session d'Automne 2018.    *                                                                   *
 *                                                                                *
 * ********************************************************************************/


public enum TYPE {


    I("01001001"),
    C("01000011"),
    RR("01000001"),
    REJ("01010010"),
    F("01000110"),
    P("01010000");
    String value ;
    TYPE(String value) {
        this.value = value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
