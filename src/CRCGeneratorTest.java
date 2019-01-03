




import static org.junit.jupiter.api.Assertions.*;

class CRCGeneratorTest {


    CRCGenerator  crcGenerator= new CRCGenerator();


    void computeCRC() {
       assertTrue(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="1010100101100100");
        assertTrue(crcGenerator.computeCRC("10100100100100011110010100101001001010001111111111111000100100100100100101001010","10001000000100001")=="0011101000110110");
        assertTrue(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="1010100101100100");
        assertFalse(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="1010100101100100");
        assertTrue(crcGenerator.computeCRC("10100100100100011110010101000100100100100100101001010","10001000000100001")=="100101100100");
        assertTrue(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="1010100101100100");
        assertFalse(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100000000000000000100101001010","10001000000100001")=="1010100101100100");
        assertFalse(crcGenerator.computeCRC("","10001000000100001")=="1010100101100100");
        assertTrue(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="1010100101100100");
        assertFalse(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="");
        assertTrue(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="1010100101100100");
        assertTrue(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100100100100100101001010","10001000000100001")=="1010100101100100");
        assertFalse(crcGenerator.computeCRC("10100100100100011110010100101001001010001001000100101001010","10001000000100001")=="1010100101100100");


    }


    @org.junit.jupiter.api.Test
    void binTab() {
    }

    @org.junit.jupiter.api.Test
    void detetectError() {

        assertTrue(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100100101001010")==true);
        assertTrue(crcGenerator.detetectError("1010010010010001","1110010100101001001010001111111111111000100100100100100101001010")==true);
        assertTrue(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100100101001010")==true);
        assertFalse(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100100101001010")==false);
        assertTrue(crcGenerator.detetectError("10001000000100001","10100100100100011110010101000100100100100100101001010")==true);
        assertTrue(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100100101001010")==true);
        assertFalse(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100000000000000000100101001010")==false);
        assertFalse(crcGenerator.detetectError("10001000000100001","10001000000100001")==true);
        assertTrue(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100100101001010")==true);
        assertFalse(crcGenerator.detetectError("10001000000100001","101001001001000111100101001010010010100010010001001001001100101001010")==false);
        assertTrue(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100100101001010")==true);
        assertTrue(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100100100100100101001010")==true);
        assertFalse(crcGenerator.detetectError("10001000000100001","10100100100100011110010100101001001010001001000100101001010")==false);

    }


    @org.junit.jupiter.api.Test
    void convertBinaryStringToString() {


assertFalse(crcGenerator.convertBinaryStringToString("01100001011000100111001101100010")=="absb");  ;
assertFalse(crcGenerator.convertBinaryStringToString("001100100110111101110111011011110110111101110111011011110111011101101111")== "2owoowowo");   ;
assertFalse( crcGenerator.convertBinaryStringToString("010000010100110001010011010011000101001101001010")=="ALSLSJ"); ;
assertFalse( crcGenerator.convertBinaryStringToString("010011010100110101000001010001110100011101010011010010000101001101001000")=="MMAGGSHSH"); ;
assertFalse(crcGenerator.convertBinaryStringToString("101001001001000111100101010001111111100100100100110001000000100001")=="1==1--2==2- "); ;
assertFalse(crcGenerator.convertBinaryStringToString("1010000010010010010010010100101011")=="ocjjncbn");  ;
assertFalse(crcGenerator.convertBinaryStringToString("01100101011011100110001101100010011100110110001001110000011000010110111101100001")=="encbsbpaoa"); ;
assertFalse(crcGenerator.convertBinaryStringToString("010011010100000101001001010010000100100001000100010100010101001101000111010101010111011000110110001100100011011100110010")=="MAIHHDQSGUv6272");  ;
assertFalse(crcGenerator.convertBinaryStringToString("01001101011111010111110101011011010110110101101101011011010110110101111000110111")=="M}}[[[[[^7");  ;
assertFalse( crcGenerator.convertBinaryStringToString("101001001001000111100100010010001001001001111111111000000000000010010101010010010100101111001000000100001")==""); ;
assertTrue(" "==" ");
assertFalse(" "=="A");

    }

    @org.junit.jupiter.api.Test
    void bitStringToIntArray() {
    }

    @org.junit.jupiter.api.Test
    void stringBulder() {
    }

    @org.junit.jupiter.api.Test
    void unBitStuff() {
    }
}