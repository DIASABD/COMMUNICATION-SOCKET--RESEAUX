


import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;


/**********************************************************************************
 *  Ce programme  permet de lire un fichier et de convertir les donner en bits et
 *  de les envoyer a un recepteur  avec lequel la connexion a ete etablie.        *
 * Il a été fait par Diasso Abdramane  matricule 20057513 et Willy Foadjo Mlle    *
 * 20059876 .  Il s'agit du dévoir 2 du cours IFT 3325 Session d'Automne 2018.    *                                                                   *
 *                                                                                *
 * ********************************************************************************/

public class Trame {

    // Attributs de la classe
    String Flag;
    TYPE type;
    String num;
    String data;
    String checksum;

    /**
     * Constructeur avec intialisation des attributs
     * @param type
     */
    public Trame(TYPE type) {
        Flag = "01111110";
        num = "";
        data = "";
        checksum = "";
        this.type = type;
    }

    public String stringToBinary(String string) {
        String temp = string;
        byte[] bytes = string.getBytes();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                stringBuilder.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return stringBuilder.toString();
    }


    public String asciiToInt(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    public String intToAscii(String intStr) {
        StringBuilder output = new StringBuilder("");

        try {
            for (int i = 0; i < intStr.length(); i += 8) {
                String str = intStr.substring(i, i + 7);
                output.append((char) Integer.parseInt(str, 16));
            }
        } catch (Exception e){};


        return output.toString();
    }
    public String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);

    }
    public String binToHex(String s) {
        return new BigInteger(s, 2).toString(16);

    }

    /**
     * @param string
     * @return
     */
    public  String introBitStuffing(String string) {
        StringBuilder resultat = new StringBuilder(string);
        int x= 0;
        int index = string.indexOf("11111");
        while (index >= 0) {
            resultat.insert( index+5+x++, '0');
            index = string.indexOf("11111", index +5);
        }
        return new String(resultat);
    }
    public  String deleteBitStuffing(String string) {
        String resultat = "";
        int lastIndex=0;
        int index = string.indexOf("11111");
        while (index >=0) {
            resultat+=string.substring(lastIndex,index+5);
            lastIndex=index+6;
            index = string.indexOf("11111", index+1);
        }
        if(index==-1) resultat+=string.substring(lastIndex,string.length());
        return resultat;
    }
    public void setType(TYPE type) {
        this.type = type;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCrc(String checksum) {
        this.checksum = checksum;
    }

    public String getFlag() {
        return Flag;
    }

    public TYPE getType() {
        return type;
    }

    public String getNum() {
        return num;
    }

    public String getData() {
        return data;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }


}



