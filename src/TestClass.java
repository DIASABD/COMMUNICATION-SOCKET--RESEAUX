import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class TestClass implements  Runnable {

    int  addBits = 0;


    /**
     *
     */
   public String  testRecepteur(String trame ){

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

    public void  flagTest (File file, ArrayList<String> trameList ){

   }
    public void  typTest (File file , ArrayList<String> trameList){

    }
    public void  numTest (File file, ArrayList<String> trameList){

    }
    public void  dataTest (File file, ArrayList<String> trameList ){

    }
    public void  crcTest (File file, ArrayList<String> trameList ){

    }

    public void  bitSiffingTest (File file, ArrayList<String> trameList ){

    }

    public void  unBitssuffingTest (File file, ArrayList<String> trameList ){

    }
    public void  trameWithErrorTest (File file, ArrayList<String> trameList ){

    }
    public void  safteTrameTest (File file, ArrayList<String> trameList ){

    }






    /**
     *
     * @param args
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException{

        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();



        try {

            System.out.println("Vueillez entrer votre commande de la facon suivante : ");
            System.out.println(" <Nom_Machine> <Num_Port> <Nom_Fichier>  : ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String buffer = in.readLine();
            args = buffer.split(" ");
            if(args.length != 4){
                System.out.println("E> Erreur de syntaxe: <Nom_Machine> <Num_Port> <Nom_Fichier>");
            }
            else{
                String nomMachine =  args[0];
                int port = Integer.parseInt(args[1]);
                String nomFichier = args[2];
                int choseWindown=Integer.parseInt(args[3]);
                File file = new File(nomFichier);
                Emetteur emetteur = new Emetteur(nomMachine);
                Recepteur recepteur = new Recepteur(port);
                recepteur.main(args);
                emetteur.main(args);

            }
        }catch (Exception e){}
    }

    @Override
    public void run() {

    }
}
