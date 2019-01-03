
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**********************************************************************************
 *  Ce programme est une implementation de l'algorithm GO BACK N                  *
 *  IL.permet de lire un fichier et de convertir les donner en bits et            *
 *  de les envoyer a un recepteur  avec lequel la connexion a ete etablie.        *
 * Cette classe Emeteur est lit le fichier , contitue les trames et les envoie
 * au recepteur                                                                   *
 * ********************************************************************************/

public class Emetteur implements  Runnable
{

	// Declaration des attributs de la classes
	private   String nomMachine;
	private int port;
	Trame trame ;
	int numeroPaquet =0;
	private ServerSocket serverSocket;
	private  Socket senderSocket;
	private DataInputStream  input   = null;
	private DataOutputStream out     = null;
	CRCGenerator crcGenerator;
	private String crc =null;
	ArrayList<String> sendPaquetData;
	int windowns = 0;

	/**
	 * methode qui retoure la valeur du crc
	 * @return
	 */
	public String getCrc() {
		return crc;
	}

	public void setWindowns(int windowns) {
		this.windowns = windowns;
	}

	/**
	 *Ceci est le constructeur de la classe qui
	 */
	public   Emetteur ( String nomMachine){
		this.nomMachine = nomMachine;
		crcGenerator = new CRCGenerator();
		sendPaquetData = new ArrayList<>();
	}

	/**
	 * Cette methode introduit les bitsuffing dans les trames .
	 * Elle recoit en parametre les chaines representant les differentes parties de la trame .
	 * @param trame
	 * @param flag
	 * @param type
	 * @param num
	 * @param data
	 * @param checksum
	 */

	public void  setTrameBitSuffing( Trame trame , String flag,TYPE type,String num,String data, String checksum ) {
		type.setValue(trame.introBitStuffing(type.getValue()));
		trame.setType(type);
		trame.setNum(trame.introBitStuffing(num));
		trame.setData(trame.introBitStuffing(data));
		trame.setChecksum(trame.introBitStuffing(checksum));
	}

	/**
	 * Cette methode cree les trames
	 * Elle fait appelle a la methode setTrameBitSuffing(...)
	 * @param trame
	 * @param flag
	 * @param type
	 * @param num
	 * @param data
	 * @return
	 */
	private  String   createTrames (Trame trame , String flag, TYPE type, int num, String data  ){
		String dataBits = trame.stringToBinary(data);
		String typeValue = type.getValue();
		String numBit = String.format("%8s", Integer.toBinaryString(num)).replace(' ', '0');
		String cnrImput= typeValue+numBit+dataBits;
		crc =crcGenerator.computeCRC(cnrImput,crcGenerator.getPolynome());
		trame.setChecksum(crc);
		setTrameBitSuffing(trame,flag,type,numBit,dataBits,crc);
		trame.setNum(numBit);
		String val= trame.getFlag()+trame.getType().getValue()+trame.getNum()+dataBits+trame.getChecksum()+trame.getFlag()+" ";
		type.setValue("01001001");
		return val ;
	}


	public String getNomMachine() {
		return nomMachine;
	}

	public int getPort() {
		return port;
	}

	/**
	 *Cette methode initialise les sockets client et serveur en envoie les trames .
	 * Elle lit les fichiers fait la conversion en bits et ensuite envoie
	 * Pour notre programme nous avons determiner les feetre a septs(7)
	 * La methodes rencvoie egalement apres un message d'erreur
	 * @param file
	 * @throws UnsupportedEncodingException
	 */
	public void sendTramePaquet( File file,int windowns) throws UnsupportedEncodingException {

		try {
			// Initialisation  des sockets
			senderSocket = new Socket(this.getNomMachine(), this.getPort());
			input  = new DataInputStream(senderSocket.getInputStream());
			out    = new DataOutputStream(senderSocket.getOutputStream());
			//Reception
			String debut="" ;
			int wind = this.getWindowns();
			boolean reSend=false;
			BufferedReader br;
			String data = "";
			String trameBit ="";
			String dataBit;
			Trame trame=null;
			int imputNum =0;
			int trameNum =1;
			String num ="";
			TYPE type;
			int indexTrame =1;
			String feedBack="";
			boolean ansersType=false;
			TYPE feedBackType=null;
			br  = new BufferedReader(new FileReader(file));
			type = TYPE.I;
			trame = new Trame(type);
			String fenet = String.format("%8s", Integer.toBinaryString(wind)).replace(' ', '0');
			String demandeConnection= trame.getFlag()+TYPE.C.getValue()+"00110000"+fenet+trame.getFlag();
			System.out.println("Demande de connection pour la proceduire Go Back N");
			System.out.println();
			System.out.println();
			System.out.println();
			//String S = trame.intToAscii(trame.binToHex(demandeConnection))	;
			out.writeUTF(demandeConnection);
			feedBack=input.readUTF();
			//System.out.println(" message recu en ASCII  = [" + asciii + "]");
			//feedBack = trame.hexToBin(trame.strToBin(asciii));
			String feedType = feedBack.substring(8,16);
			if(feedType.equals(TYPE.RR.getValue()))
				System.out.println("La connexion a ete bien etablie ");
			System.out.println();
			System.out.println();
			System.out.println();
			while((data = br.readLine()) != null) { // Lecture des fichiers
				if(sendPaquetData.size()<windowns){
					type = TYPE.I;
					trame.setType(type);
					dataBit = trame.stringToBinary(data);
					trame.setData(dataBit);
					trameBit = createTrames(trame, trame.getFlag(), trame.getType(), trameNum, trame.getData());
				   // String SI = trame.intToAscii(trame.binToHex(trameBit))	;
					out.writeUTF(trameBit);
				    sendPaquetData.add(trameBit);
					System.out.println("Le trame " + trameNum + " a ete transmis au recepteur ");
					System.out.println();
					trameNum++;
				}
				if(trameNum==7)trameNum=1;
				if (sendPaquetData.size() ==this.getWindowns()) {
					Thread.sleep(3000);
					System.out.println("En attente de la reponse de la trame numero "+ indexTrame);
					System.out.println();
					feedBackType=trame.getType();
					   feedBack	 = input.readUTF();
						String feedNum=feedBack.substring(16,24);
					    imputNum=Integer.parseInt(feedNum, 2);;
						String s = feedBack.substring(8,16);
						//feedBackType.setValue(s);
						ansersType =(ansersType||s.equals(TYPE.REJ.getValue()));
						System.out.println(" L'emeteur a   recu un feedback  pour la de trame : " + imputNum);
					if(ansersType==true){
						System.out.println(" Le type de message de la trame es   : " + convertBinaryStringToString(TYPE.REJ.getValue()));
						boolean trameTrouve= false;
						for (int i = 0;i<sendPaquetData.size();i++){
							int paqTramNum=Integer.parseInt(sendPaquetData.get(i).substring(16,24),2);
							if(paqTramNum==imputNum) trameTrouve= true;
							if(trameTrouve) {
								//String ST = trame.intToAscii()	;
								out.writeUTF(sendPaquetData.get(i));
								System.out.println(" notre emeteur a reenvoyer  la trame numero   : " + paqTramNum+ " au recepteur ");
								i++;
							}
						}

						ansersType=false;
					}
					else if(ansersType==false){
						System.out.println(" Le type de message de la trame es   : " + convertBinaryStringToString(TYPE.RR.getValue()));
						System.out.println(" Le trame numero  "+ imputNum+ " a ete acquite  ");
						deleteTrame(sendPaquetData,trameBit);
						String curTram =sendPaquetData.get(0);
					}
					indexTrame++;
					if(indexTrame==windowns)indexTrame=1;
				}
				System.out.println();
				System.out.println();
				System.out.println();
			}
			String fermetureConection = trame.getFlag()+TYPE.F.getValue()+trame.getFlag();
			out.writeUTF(fermetureConection);
			br.close();
			out.flush();
			senderSocket.close();
		}catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 *Cette methode initialise les sockets client et serveur en envoie les trames .
	 * Elle lit les fichiers fait la conversion en bits et ensuite envoie
	 * Pour notre programme nous avons determiner les feetre a septs(7)
	 * La methodes rencvoie egalement apres un message d'erreur
	 * @param file
	 * @throws UnsupportedEncodingException
	 */
	public int  read( File file) throws UnsupportedEncodingException {
		int lineNum=0;
		try {
			BufferedReader br;
			String data ="";

			br  = new BufferedReader(new FileReader(file));
				while((data = br.readLine()) != null) {
                lineNum++;
				}
			} catch (Exception e){}

		return  lineNum;
	}

	/**
	 *
	 * @param nomMachine
	 */
	public void setNomMachine(String nomMachine) {
		this.nomMachine = nomMachine;
	}

	/**
	 *
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public int getWindowns() {
		return windowns;
	}

	public  ArrayList<String> deleteTrame(ArrayList<String> list,String string ){
		int i = 0;
		while (i<list.size()){
			if(list.get(i).equals(string)){
				list.remove(i);
			}
			i++;
		}
		System.out.println("list = [" + list.size()+ "]");
		return  list;

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


	public Trame getTrame() {
		return trame;
	}

	public int getNumeroPaquet() {
		return numeroPaquet;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public Socket getSenderSocket() {
		return senderSocket;
	}

	public DataInputStream getInput() {
		return input;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public CRCGenerator getCrcGenerator() {
		return crcGenerator;
	}

	public ArrayList<String> getSendPaquetData() {
		return sendPaquetData;
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
				int goBackEnd=Integer.parseInt(args[3]);
				int choseWindown =0;
				File file = new File(nomFichier);
				Emetteur emetteur = new Emetteur(nomMachine);
				Recepteur recepteur = new Recepteur(port);
				int numbLine = emetteur.read(file);
				System.out.println("args nom ligne = [" + numbLine+ "]");
				if(goBackEnd==0){
					choseWindown=4;
				}
				else {
					choseWindown = numbLine-1;
				}
				emetteur.setPort(port);
				emetteur.setWindowns(choseWindown);
				emetteur.sendTramePaquet(file,choseWindown);
			}
		}catch (Exception e){}
	}

	@Override
	public void run() {

	}
}

