
import java.io.*;

/**********************************************************************************
 *  Ce programme  permet de lire un fichier et de convertir les donner en bits et
 *  de les envoyer a un recepteur  avec lequel la connexion a ete etablie.        *
 * Il a été fait par Diasso Abdramane  matricule 20057513 et Willy Foadjo Mlle    *
 * 20059876 .  Il s'agit du dévoir 2 du cours IFT 3325 Session d'Automne 2018.    *                                                                   *
 *                                                                                *
 * ********************************************************************************/


public class CRCGenerator {

    final  String polynome  = "10001000000100001";

            public String getPolynome() {
        return polynome;
    }

    /**
     * Cette methode fait la division binaire . Elle prend en parametre deux tableau de bits
     * un diviseur et un dividente et retourne le resultat apres l'operation
     * @param divisor
     * @param memArray
     * @return
     */

    static int[] divide(int divisor[], int memArray[]) {
        int currentBit = 0;
        while (true) {
            for (int i = 0; i < divisor.length; i++)
                memArray[currentBit  + i] = (memArray[currentBit  + i] ^ divisor[i]);

            while (memArray[currentBit ] == 0 && currentBit  != memArray.length - 1)
                currentBit ++;

            if ((memArray.length - currentBit ) < divisor.length)
                break;
        }
        return memArray;
    }

    /**
     *
     * @param binString
     * @param polynome
     * @return
     */

    public String computeCRC(String binString, String polynome) {

        int[] data = binTab(binString);
        int[] div;
        int[] divisor = binTab(polynome);
        int[] memArray;
        int[] crc;
        int[] crcInter;
        int data_bits = data.length;
        int divisor_bits = polynome.length();
        int array_length = data_bits + divisor_bits - 1;
        div = new int[ array_length];
        memArray = new int[ array_length];
        crc = new int[ array_length];
        crcInter = new int[array_length-data_bits];

        for (int i = 0; i < data.length; i++)
            div[i] = data[i];
        for (int j = 0; j < div.length; j++) {
            memArray[j] = div[j];
        }
        memArray = divide( divisor, memArray);

        for (int i = 0; i < div.length; i++)
        {
            crc[i] = (div[i] ^ memArray[i]);
        }

        int index = crcInter.length-1;
        for (int j = crc.length-1; j >=0&&index>=0 ; j--)
        {
            crcInter[index--] = crc[j];
        }
        String str =stringBulder(crcInter);
        return new String(str);
    }


    public static String convertBinaryStringToString(String s){
        String str = "";
        for (int i = 0; i < s.length()/8; i++) {

            int a = Integer.parseInt(s.substring(8*i,(i+1)*8),2);
            str += (char)(a);
        }
        return  str;
    }




    public int[] binTab(String string) {

        char[] TAB = string.toCharArray();

        int[] resultat = new int[TAB.length];

        for (int i = 0; i < TAB.length; i++) {
            int v = Character.getNumericValue(TAB[i] );
            resultat[i] = v;
        }
        return resultat;
    }

    /**
     *
     * @param polynome
     * @param
     * @return
     */

    public boolean detetectError(String polynome,String data) {
        int[]   crc =  bitStringToIntArray(data);
        int resultat =0;

        for ( int i=0;i<crc.length;i++){
            resultat+=crc[i];
        }
        boolean result = false;
        int[] divisor =this.binTab(polynome);
        int[] rem;
        int data_bits =data.length();
        rem = new int[crc.length];
        for (int j = 0; j < crc.length; j++) {
            rem[j] = crc[j];
        }
        rem = this.divide(divisor, rem);
        for (int i = 0; i < rem.length; i++) {
            if (rem[i] != 0) {
                result = true;
                break;
            }
            if (i == rem.length - 1)
                result=false;
        }
        return  result;
    }


    public  int [] bitStringToIntArray(String string){
        char [] strArray= string.toCharArray();

        int[] resultat  = new int[string.length()];
        for (int i = 0;i<string.length();i++){
            resultat[i] = Character.getNumericValue(strArray[i]);
        }
        return  resultat;

    }

    public  String stringBulder( int [] strArr){

        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            strBuilder.append(strArr[i]);
        }
        String newString = strBuilder.toString();
        return  newString;
    }

    public  String unBitStuff(String trameBit,int start,int end , int charIndex){
        return trameBit.substring(start,end).replace(trameBit.substring(start,end).charAt(charIndex)+"","");
    }


}

