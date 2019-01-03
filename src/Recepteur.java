import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Random;


/**********************************************************************************
 *  Ce programme est une implementation de l'algorithm GO BACK N                  *
 *  IL.permet de lire un fichier et de convertir les donner en bits et            *
 *  de les envoyer a un recepteur  avec lequel la connexion a ete etablie.        *
 * Cette classe Recepteur recoit les trame et renvoie les trames de confirmation  *
 * à   l'emetteur                                                                 *
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

	/**
	 *
	 * @param string
	 * @return
	 */
	public static String convertBinaryStringToString(String string){
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
		String asciii ="";
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
				msgRec=imput.readUTF() +" ";
			//  System.out.println(" message recu en ASCII  = [" + asciii + "]");
			 /// msgRec = trame.hexToBin(trame.strToBin(asciii));
		//	System.out.println("str to bin  = [" + msgRec + "]");
				String S= msgRec.substring(8, 16);
				if(S.equals("01000011")){
					//String ST = trame.intToAscii())	;
					out.writeUTF(trame.getData()+TYPE.RR.getValue()+"01000001"+trame.getFlag());
						System.out.println("Connexion acceptée");
						window= Integer.parseInt(msgRec.substring(24,32),2);
				}

			do {
				msgRec=imput.readUTF();
				//System.out.println(" message recu en ASCII  = [" + asciii + "]");
				//msgRec = trame.hexToBin(trame.strToBin(asciii));
				receivePaquet.add(msgRec);
			} while (receivePaquet.size()<window);

			do {

				boolean detectErro=false;
				introductuceFalseBit= new Random().nextInt(10)<3; /// decider aleatoirement d'ajouetr des bitSuffing  a la trame
				String currentTrame = receivePaquet.get(receivPaquetIndex);
				this.flag = currentTrame.substring(0, 8);
				str2 = trame.deleteBitStuffing(msgRec.substring(8, msgRec.length() - 8));
				if(introductuceFalseBit==true) str2+="1111110111101";
				trameValue = flag + str2 + flag;
				this.currentTrameType = trameValue.substring(8, 16);
				this.currentTrameNum = trameValue.substring(16, 24);
				System.out.println(" currenttrameType  = [" + currentTrameType + "]");
				tramNum =Integer.parseInt(currentTrameNum,2);
				switch (currentTrameType) {
					case  "01000011" :
						System.out.println("Connexion acceptée");
						System.out.println();
						System.out.println();
						//String ST = trame.intToAscii()	;
						out.writeUTF(trame.getData()+TYPE.RR.getValue()+"01000001"+trame.getFlag());
						break;
					case "01000110" :
						System.out.println("Fin de la connexion ");
						//String SP = trame.intToAscii())	;
						out.writeUTF(trame.getData()+TYPE.F.getValue()+"00110000"+trame.getFlag());
						System.out.println();
						System.out.println();
						break;
					case "01001001":
						if (msgRec.length() != (flag + currentTrameType + currentTrameNum).length()) {
							this.currentTrameData = trameValue.substring(24, trameValue.length() - 24);
							this.currentTrameCheckSum = trameValue.substring(trameValue.length() - 24, trameValue.length() - 8);
							detectErro = (detectErro || crcGenerator.detetectError(crcGenerator.getPolynome(), str2));
							System.out.println(detectErro);
							if (detectErro == true) {
								anwser = TYPE.REJ.getValue();
								boolean trameTrouve= false;
								for(int i = 0; i < receivePaquet.size(); i++){
									int listIndex = Integer.parseInt(currentTrame.substring(16,24),2);
									tramNum=Integer.parseInt(receivePaquet.get(i).substring(16,24),2);
									//System.out.println(" 16 a 24 = [" + currentTrame.substring(16,24) + "]");
									if(listIndex==tramNum){
									trameTrouve= true;
									}
									if(trameTrouve){
										String tr = receivePaquet.get(i);
										mesSend = tr.substring(0, 8) + anwser + tr.substring(16, tr.length());
										//String SO = trame.intToAscii(trame.binToHex(mesSend))	;
										out.writeUTF(mesSend);
										System.out.println("La trame  numero  : " + tramNum + " a ete renvoyee pour constat d'erreur ");
									}
								}


							} else if (detectErro == false) {
								anwser = TYPE.RR.getValue();
								try {
										mesSend = currentTrame.substring(0, 8) + anwser + currentTrame.substring(16, currentTrame.length());
								//	String SL = trame.intToAscii(trame.binToHex(mesSend))	;
									out.writeUTF(mesSend);
										String message = currentTrame.substring(24,currentTrame.length()-24);
									//System.out.println(""+ convertBinaryStringToString(message.toString().trim()) +"");
									tramNum=Integer.parseInt(currentTrame.substring(16,24),2);
								    	System.out.println("La trame  numero  : " + tramNum+ " a ete aquite");
									    delateTrame(receivePaquet,currentTrame);
									    while (receivePaquet.size()<window){
											msgRec=imput.readUTF();
											//System.out.println(" message recu en ASCII  = [" + asciii + "]");
											//msgRec = trame.hexToBin(trame.strToBin(asciii));
								 		receivePaquet.add(msgRec);
									    }


								} catch (IOException x) {
								}
								System.out.println();
								System.out.println();
							}


						}
					case  "01000001" : delateTrame(paquetsData,currentTrame);
					break;
					default:
						break;
				}

				if(receivPaquetIndex ==window-1){
					receivPaquetIndex =0;
				}
				else {
					receivPaquetIndex++;
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


	/**
	 * Cette methode recoit les trames , analyses les bits recus
	 * et renvoie les message d'erreurs
	 * @param port
	 * @throws IOException
	 */
	public  void receiAll(int port) throws IOException{
		// Attributs de la methode

		String msgRec = "";

		String mesSend="";
		String anwser="";
		connect(port);
		String trameValue ="";
		Trame trame = new Trame(TYPE.I);
		String receveiveRentPaquet="";
		String str1="";
		String str2="";
		String str3="";
		int window=0;

		int tramNum = 0;
		ArrayList<String> receivePaquet= new ArrayList<>();
		int receivPaquetIndex =0;
		try {
			msgRec=imput.readUTF() +" ";
			String S= msgRec.substring(8, 16);
			if(S.equals("01000011")){
				out.writeUTF(trame.getData()+TYPE.RR.getValue()+"01000001"+trame.getFlag());
				System.out.println("Connexion acceptée");
				window= Integer.parseInt(msgRec.substring(24,32),2);
			}
			do {
				msgRec=imput.readUTF() +" ";
				receivePaquet.add(msgRec);
				boolean detectErro=false;
				String currentTrame = receivePaquet.get(receivPaquetIndex);
				this.flag = currentTrame.substring(0, 8);
				str2 = trame.deleteBitStuffing(msgRec.substring(8, msgRec.length() - 8));
				trameValue = flag + str2 + flag;
				this.currentTrameType = trameValue.substring(8, 16);
				this.currentTrameNum = trameValue.substring(16, 24);
				tramNum =Integer.parseInt(currentTrameNum,2);
				switch (currentTrameType) {
					case "01000011":
						System.out.println("Connexion acceptée");
						System.out.println();
						System.out.println();
						out.writeUTF(trame.getData() + TYPE.RR.getValue() + "01000001" + trame.getFlag());
						break;
					case "01001001":
						if (msgRec.length() != (flag + currentTrameType + currentTrameNum).length()) {
							this.currentTrameData = trameValue.substring(24, trameValue.length() - 24);
							this.currentTrameCheckSum = trameValue.substring(trameValue.length() - 24, trameValue.length() - 8);
							detectErro = (detectErro || crcGenerator.detetectError(crcGenerator.getPolynome(), str2));
							System.out.println(detectErro);
							if (detectErro == true) {
								anwser = TYPE.REJ.getValue();
							//	for (int i = 0; i < receivePaquet.size(); i++) {
									String tr = receivePaquet.get(receivPaquetIndex);
									mesSend = tr.substring(0, 8) + anwser + tr.substring(16, tr.length());
									out.writeUTF(mesSend);
									//delateTrame(receivePaquet, currentTrame);
									System.out.println("La trame  numero  : " + tramNum + " a ete renvoyee pour constat d'erreur ");
							//	}
							}
						}

					case "01000110":
						if (detectErro == false) {
						  String	anwsers = TYPE.RR.getValue();
							try {
								mesSend = currentTrame.substring(0, 8) + anwsers + currentTrame.substring(16, currentTrame.length());
								out.writeUTF(mesSend);
								System.out.println("Toutes les trames ont  ete aquite");
								delateTrame(receivePaquet, currentTrame);

							} catch (IOException x) {
							}
							System.out.println();
							System.out.println();
						}
						System.out.println("Fin de la connexion ");
						out.writeUTF(trame.getData() + TYPE.F.getValue() + "00110000" + trame.getFlag());
						System.out.println();
						System.out.println();

					default:
						break;
				}
					receivPaquetIndex++;


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
				int port = Integer.parseInt(args[0]);Recepteur recepteur = new Recepteur(port);
				recepteur.receive(recepteur.getPort());
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
