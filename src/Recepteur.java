import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Random;


/**********************************************************************************
 *  Ce programme  permet de lire un fichier et de convertir les donner en bits et
 *  de les envoyer a un recepteur  avec lequel la connexion a ete etablie.        *
 * Il a été fait par Diasso Abdramane  matricule 20057513 et Willy Foadjo Mlle    *
 * 20059876 .  Il s'agit du dévoir 2 du cours IFT 3325 Session d'Automne 2018.    *                                                                   *
 *                                                                                *
 * ********************************************************************************/


public class Recepteur implements Runnable{

	// Declaration des attrbuts de la classe
	public static ServerSocket serverSocket;
	public static Socket receverSocket;
	final  String polynome  = "10001000000100001";
	private int port=0;
	private DataInputStream imput      =  null;
	private  DataOutputStream out;
	CRCGenerator  crcGenerator;
	String currentTrameNum;
	ArrayList<String> paquetsData;
	String currentTrameCheckSum;
	String currentTrameData;
	String currentTrameType;
	String flag;
	int window ;
	Emetteur emetteur;
	TestClass testClass= new TestClass();

	public  Recepteur (int port){
		this.port = port;
		crcGenerator =new CRCGenerator();
		paquetsData = new ArrayList<>();
		emetteur = new Emetteur("localhost");
	}

	/**
	 * getter pour le numero de port
	 * @return
	 */

	public int getPort() {
		return port;
	}

	/**
	 * methode qui initialise les socket client et serveur du recepteur
	 * @param port
	 */
	public  void connect( int port){
		try{
			serverSocket = new ServerSocket(port);
			receverSocket= serverSocket.accept();
			out = new DataOutputStream(receverSocket.getOutputStream());
			imput = new DataInputStream(receverSocket.getInputStream());

		}catch(IOException e){

		}
	}

	public int getWindow() {
		return window;
	}



	public  ArrayList<String> delateTrame(ArrayList<String> list,String string ){
		int i = 0;
		while (i<list.size()){
			if(list.get(i).equals(string)){
				list.remove(i);
			}
			i++;
		}
		return  list;

	}


	/**
	 * Cette methode recoit les trames , analyses les bits recus
	 * et renvoie les message d'erreurs
	 * @param port
	 * @throws IOException
	 */
	public  void receive(int port) throws IOException{
		// Attributs de la methode
		String X ="";
		String msgRec = "";
		String mesSend="";
		int count = 0;
		String anwser="";
		connect(port);
		String trameValue ="";
		Trame trame = new Trame(TYPE.I);
		String receveiveRentPaquet="";
		String str1="";
		String str2="";
		String str3="";
		int window=0;
		boolean introductuceFalseBit=false;
		int tramNum = 0;
		ArrayList<String> receivePaquet= new ArrayList<>();
		int receivPaquetIndex =0;
		try {
				msgRec=imput.readUTF();
				String S= msgRec.substring(8, 16);
				if(S.equals("01000011")){
						out.writeUTF(trame.getData()+TYPE.RR.getValue()+"01000001"+trame.getFlag());
						System.out.println("Connexion acceptée");
						window= Integer.parseInt(msgRec.substring(24,32),2);

				}

			do {
				msgRec=imput.readUTF();
				receivePaquet.add(msgRec);
			} while (receivePaquet.size()<window);

			do {

				boolean detectErro=false;
				String currentTrame = receivePaquet.get(receivPaquetIndex);
				this.flag = currentTrame.substring(0, 8);
				str2 = trame.deleteBitStuffing(msgRec.substring(8, msgRec.length() - 8));
				trameValue = flag + str2 + flag;
				this.currentTrameType = trameValue.substring(8, 16);
				this.currentTrameNum = trameValue.substring(16, 24);
				System.out.println("trame  = [" + trameValue+ "]");
				System.out.println(" currenttrameType  = [" + currentTrameType + "]");
				tramNum =Integer.parseInt(currentTrameNum,2);
				switch (currentTrameType) {
					case  "01000011" :
						System.out.println("Connexion acceptée");
						System.out.println();
						System.out.println();
						out.writeUTF(trame.getData()+TYPE.RR.getValue()+"01000001"+trame.getFlag());
						break;
					case "01000110" :
						System.out.println("Fin de la connexion ");
						out.writeUTF(trame.getData()+TYPE.F.getValue()+"00110000"+trame.getFlag());
						System.out.println();
						System.out.println();
						break;
					case "01001001":
						if (msgRec.length() != (flag + currentTrameType + currentTrameNum).length()) {
							this.currentTrameData = trameValue.substring(24, trameValue.length() - 24);
							this.currentTrameCheckSum = trameValue.substring(trameValue.length() - 24, trameValue.length() - 8);
							introductuceFalseBit = new Random().nextInt(10) < 2;
							if (introductuceFalseBit == true) str2 += "1111111111111111111111";
							detectErro = (detectErro || crcGenerator.detetectError(crcGenerator.getPolynome(), str2));
							System.out.println(detectErro);
							if (detectErro == true) {
								anwser = TYPE.REJ.getValue();
								boolean trameTrouve= false;
								for(int i = 0; i < receivePaquet.size(); i++){
									int listIndex = Integer.parseInt(receivePaquet.get(i).substring(16,24),2);
									if(listIndex==tramNum){
									trameTrouve= true;
									}
									if(trameTrouve){
										String tr = receivePaquet.get(i);
										mesSend = tr.substring(0, 8) + anwser + tr.substring(16, tr.length());
										out.writeUTF(mesSend);
										System.out.println("La trame  numero  : " + listIndex + " a ete renvoyee pour constat d'erreur ");
										System.out.println();
										System.out.println();
									}
								}


							} else if (detectErro == false) {
								anwser = TYPE.RR.getValue();
								try {
										mesSend = currentTrame.substring(0, 8) + anwser + currentTrame.substring(16, currentTrame.length());
										out.writeUTF(mesSend);
										Thread.sleep(3000);
										String message = currentTrame.substring(24,currentTrame.length()-24);
									tramNum=Integer.parseInt(currentTrame.substring(16,24),2);
								    	System.out.println("La trame  numero  : " + tramNum+ " a ete aquite");
									System.out.println();
									System.out.println();
									    delateTrame(receivePaquet,currentTrame);
									    while (receivePaquet.size()<window){
									    msgRec=imput.readUTF();
								 		receivePaquet.add(msgRec);
									    }
									System.out.println();
									System.out.println();

								} catch (IOException x) {
								}
							}


						}
					case  "01000001" : delateTrame(paquetsData,currentTrame);
					break;
					default:
						break;
				}


			}while (true) ;
		}catch (IOException e) {}
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void run() {
	}

	public static void main(String[] args){


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
			System.out.println("Vueillez entrer le numero de port : ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String buffer = in.readLine();
			args =  buffer.split(" ");
			if(args.length != 1){
				System.out.println("R> Erreur de syntaxe: <Num_Port>");
			}
			else{
				int port = Integer.parseInt(args[0]);
				Recepteur recepteur = new Recepteur(port);
				recepteur.receive(recepteur.getPort());
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
