package ClasesCliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	
	// IP y Puerto a la que nos vamos a conectar
	public static final int PUERTO = 666;
	public static final String IP_SERVER = "localhost";
	
	public static void main(String[] args) {
		System.out.println("         CLIENTE         ");
		System.out.println("-------------------------");

		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
		
		try (Scanner sc = new Scanner(System.in)){
						
			System.out.println("CLIENTE: Esperando a que el servidor acepte la conexi�n");
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);
			System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER + " por el puerto " + PUERTO);

			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream()); //los datos que vienen del servidor
			BufferedReader entradaBuffer = new BufferedReader(entrada);
			
			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			
			String texto = "";
			boolean continuar = true;
			String opcion;
						
			do {
				
				System.out.println(" \nElige una opci�n entre las siguientes: \n" + "----> 1. Consultar libro por ISBN \n" + 
									"----> 2. Consultar libro por t�tulo. \n" + "----> 3. Consultar libro por autor. \n" + 
									"----> 4. Salir de la aplicaci�n");
				
				opcion = sc.nextLine(); //recoge el n�mero que elegiremos en el men� anterior	.
				
				switch (opcion){
					case "1":
						
						System.out.println("Introduzca el ISBN");
						texto = sc.nextLine() + "@";
						
						break;
					case "2":
						System.out.println("Introduzca el t�tulo");
						texto = sc.nextLine() + "/";
						
						break;
					case "3":
						System.out.println("Introduzca el autor");
						texto = sc.nextLine() + "*";
						
						break;
					case "4":
						
						continuar = false;
						socketAlServidor.close();
						break;
					default:
					
					System.out.println("Elige la opci�n escribiendo un n�mero, por favor");
					
					
				}
				
				salida.println(texto); //recoge el texto de los casos anteriores para enviarlo al servidor.
				
				System.out.println("CLIENTE: Esperando datos del libro ...... ");				
				String respuesta = entradaBuffer.readLine();
				
				System.out.println("CLIENTE: Servidor responde: " + respuesta);
						
				
			}while(continuar);
			
			//Cerramos la conexion
			socketAlServidor.close();
			
			
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la direcci�n" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}
		
		System.out.println("CLIENTE: Fin del programa");
	}

	
	
	
}
