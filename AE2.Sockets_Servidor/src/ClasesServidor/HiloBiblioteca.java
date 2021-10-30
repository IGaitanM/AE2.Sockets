package ClasesServidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class HiloBiblioteca implements Runnable {
	
	private ArrayList<Libro> biblioteca;
	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketAlCliente;	
	
	public HiloBiblioteca (Socket socketAlCliente) {
		numCliente++;
		hilo = new Thread(this, "Cliente_"+numCliente);
		this.socketAlCliente = socketAlCliente;
		biblioteca= new ArrayList<Libro>();
			biblioteca.add(new Libro("9780756413712", "El Nombre Del Viento", "Patrick Rothfuss", 10.40));
			biblioteca.add(new Libro("9788448037246", "El Elfo Oscuro", "R.A Slvatore ", 190));
			biblioteca.add(new Libro("9788408043645", "La sombra del viento", "Carlos Ruiz Zafón", 8));
			biblioteca.add(new Libro("9788496940000", "El Médico", "Noah Gordon", 11.35));
			biblioteca.add(new Libro("9788416858217", "Materia OScura", "Blake Crouch", 16.15));
			
		hilo.start();
	}
	
	public HiloBiblioteca () {
		biblioteca= new ArrayList<Libro>();
		biblioteca.add(new Libro("9780756413712", "El Nombre Del Viento", "Patrick Rothfuss", 10.40));
		biblioteca.add(new Libro("9788448037246", "El Elfo Oscuro", "R.A Slvatore ", 190));
		biblioteca.add(new Libro("9788408043645", "La sombra del viento", "Carlos Ruiz Zafón", 8));
		biblioteca.add(new Libro("9788496940000", "El Médico", "Noah Gordon", 11.35));
		biblioteca.add(new Libro("9788416858217", "Materia OScura", "Blake Crouch", 16.15));
		
	}
		

	@Override
	public void run() {
		System.out.println("Estableciendo comunicacion con " + hilo.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
		try {
			//Salida del servidor al cliente
			salida = new PrintStream(socketAlCliente.getOutputStream());
			//Entrada del servidor al cliente
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			entradaBuffer = new BufferedReader(entrada);
			
			String texto = "";
			boolean continuar = true;
			
			
			while (continuar) {
				texto = entradaBuffer.readLine();
				
				String respuestaServidor;
				
				if (texto.trim().contains("@")) {
					
					HiloBiblioteca hiloBusca = new HiloBiblioteca();
					String[] textoRecibido = texto.split("@");
					respuestaServidor= hiloBusca.buscaIsbn(textoRecibido[0]).toString();
					
					System.out.println(hilo.getName() + " dice: El libro que buscas es" + respuestaServidor );
					
					//Le mandamos la respuesta al cliente
					salida.println(respuestaServidor);
					
					
				} else if (texto.contains("/"))  {
					
					respuestaServidor= buscaTitulo(texto).toString();
					
					
					
				}
			}
			//Cerramos el socket
			socketAlCliente.close();
			//Notese que si no cerramos el socket ni en el servidor ni en el cliente, mantendremos
			//la comunicacion abierta
		} catch (IOException e) {
			System.err.println("HiloContadorLetras: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloContadorLetras: Error");
			e.printStackTrace();
		}
	}
	
	//Métodos para manejar el arrayList de la bibioteca
	
	public String buscaIsbn(String isbn){
        Libro libro;           
        for(int i=0; i<biblioteca.size(); i++){
            libro=biblioteca.get(i);
            
            if(libro.getIsbn().equals(isbn)){
                return libro.toString();                
            }
        }        
        return "El libro no está en la biblioteca";
    }
			
	public String buscaTitulo(String titulo){
        Libro libro;           
        for(int i=0; i<biblioteca.size(); i++){
            libro=biblioteca.get(i);
            
            if(libro.getTitulo().equals(titulo)){
                return libro.toString();                
            }
        }        
        return "El libro no está en la biblioteca";
    }
		
		
		public ArrayList<Libro> getBiblioteca() {
			return biblioteca;
		}

		public void setBiblioteca(ArrayList<Libro> biblioteca) {
			this.biblioteca = biblioteca;
		}

//		public static void main(String[] args) {
//			
//			HiloBiblioteca hilosss = new HiloBiblioteca();
//			String caca= hilosss.buscaIsbn("9780756413712");
//			System.out.println(caca);
//			
//			
//		}
	
	
}

