


import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;


/**********************************************************************************
 *  Ce programme est une implementation de l'algorithm GO BACK N                  *
 *  IL.permet de lire un fichier et de convertir les donner en bits et            *
 *  de les envoyer a un recepteur  avec lequel la connexion a ete etablie.        *
 * Cette classe Emeteur est lit le fichier , contitue les trames et les envoie
 * au recepteur                                                                   *
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
        StringBuffer stringBuilder = new StringBuffer();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                stringBuilder.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return stringBuilder.toString();
    }
    public String strToBin(String string){
        string = stringToBinary(string);
        int j=0;
        String resultat ="";
        for(int i = 0 ;i<string.length();i+=8){
            if((j%3)==2){
                resultat+=string.substring(i,i+8);
            }
            j++;
        }

        return  resultat;
    }
    public  String convertBinaryStringToString(String string){
        StringBuffer sb = new StringBuffer();
        char[] chars = string.toCharArray();

        for (int j = 0; j < chars.length-1; j+=8) {
            int idx = 0;
            int sum = 0;
            for (int i = 7; i>= 0; i--) {
                if (chars[i+j] == '1') {
                    sum += 1 << idx;
                }
                idx++;
            }
            sb.append(Character.toChars(sum));
        }
        return new String(sb);
    }

    public String asciiToInt(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toBinaryString((int) ch));
        }
        return hex.toString();
    }

    public String intToAscii(String intStr) {
        StringBuffer output = new StringBuffer("");

        try {
            for (int i = 0; i < intStr.length(); i += 8) {
                String str = intStr.substring(i, i + 8);
                output.append((char) Integer.parseInt(str, 10));
            }
        } catch (Exception e){};


        return output.toString();
    }
    public String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);

    }
    public String binToHex(String s) {
        return new BigInteger(s, 2).toString(10);

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



