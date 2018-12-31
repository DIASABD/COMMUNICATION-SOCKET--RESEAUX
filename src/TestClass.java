import java.util.Random;

public class TestClass {

    int  addBits = 0;

   public String  introduceBits(String trame ){

        addBits = new Random().nextInt(10);

        switch (addBits){
            case 1 : trame+="1111";
            break;
            case 2 : trame+="11100000011";
                break;

            case 3 : trame =trame.substring(0,76);
                break;

            case 4 : trame= "111111111";
                break;
            case 5 : trame+="000000000000111";
                break;

            case 6 : trame+="11111111";
                break;

            case 7 : trame+="011111111";
                break;

            case 8 : trame+="1111111111";
                break;

            case 9 : trame+="1010";
                break;
            case 10 : trame= trame;
                break;
                default: break;
        }
        return trame;
    }
}
