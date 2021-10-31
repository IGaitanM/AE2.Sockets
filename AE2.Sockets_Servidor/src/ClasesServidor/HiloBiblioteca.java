package ClasesServidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
//import java.util.Scanner;

public class HiloBiblioteca implements Runnable {
	
	private ArrayList<Libro> biblioteca;
	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketAlCliente;	
	
	public HiloBiblioteca (Socket socketAlCliente) {
		numCliente++;
		hilo = new Thread(this, "Cliente_"+numCliente);
		this.socketAlCliente = socketAlCliente;
		hilo.start();
		
	}
	
	public HiloBiblioteca () {
		biblioteca= new ArrayList<Libro>();
		biblioteca.add(new Libro("9780756413712", "El Nombre Del Viento", "Patrick Rothfuss", 10.40));
		biblioteca.add(new Libro("9788448037246", "El Elfo Oscuro", "R.A Slvatore", 190));
		biblioteca.add(new Libro("9788408043645", "La sombra del viento", "Carlos Ruiz Zafón", 8));
		biblioteca.add(new Libro("9788496940000", "El Médico", "Noah Gordon", 11.35));
		biblioteca.add(new Libro("9788416858217", "Materia OScura", "3", 16.15));
		biblioteca.add(new Libro("9788499899619", "El Temor de un Hombre Sabio", "Patrick Rothfuss", 10.95));
		
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
				
				if (texto.equalsIgnoreCase("FIN")) {
					salida.println("Cerrando conexión");
					System.out.println(hilo.getName() + " ha cerrado la comunicacion");
					continuar = false;
					
				}else {	
				
					// Procesa la petición del cliente para buscar por ISBN
					if (texto.contains("@")) {
						
						HiloBiblioteca hiloBusca = new HiloBiblioteca();
						texto = texto.replace("@", "");
						respuestaServidor= hiloBusca.buscaIsbn(texto.trim().toString());
						
						System.out.println(hilo.getName() + " busca el libro: " + respuestaServidor );
						
						salida.println(respuestaServidor);
						
					// Procesa la petición del cliente para buscar por Título
					} else if (texto.contains("/"))  {
						
						HiloBiblioteca hiloBusca = new HiloBiblioteca();
						texto = texto.replace("/", "").trim();
						respuestaServidor= hiloBusca.buscaTitulo(texto.trim().toString());
						
						System.out.println(hilo.getName() + " busca el libro: " + respuestaServidor );
						
						salida.println(respuestaServidor);
					
					// Procesa la petición del cliente para buscar por autor	
					} else if (texto.contains("*"))  {
							
						HiloBiblioteca hiloBusca = new HiloBiblioteca();
						texto = texto.replace("*", "").trim();
						respuestaServidor= hiloBusca.buscaAutor(texto.toString());
							
						System.out.println(hilo.getName() + " busca el libro: " + respuestaServidor);
							
						
						salida.println(respuestaServidor);	
					
					// Procesa la petición del cliente para añadir un libro completo
					}else if (texto.contains("%"))  {
							
						HiloBiblioteca hiloBusca = new HiloBiblioteca();
						String[] textoRecibido = texto.split("%");
						Libro libro= new Libro(textoRecibido[0].toString(), textoRecibido[1].toString(),
												textoRecibido[2].toString(), Double.parseDouble(textoRecibido[3].toString()));
						hiloBusca.addLibro(libro);
						respuestaServidor= " Libro añadido correctamente ---> " + hiloBusca.mostrarUltimoBiblioteca() ;
								
						System.out.println(hilo.getName() + " busca el libro: " + respuestaServidor);

						salida.println(respuestaServidor);	
						
					} else {
						System.out.println("Servidor cerrado");
						socketAlCliente.close();
						
					}
				}	
			}
				
			//Cerramos el socket
			socketAlCliente.close();
			//Notese que si no cerramos el socket ni en el servidor ni en el cliente, mantendremos
			//la comunicacion abierta
		} catch (IOException e) {
			System.err.println("HiloBiblioteca: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloBiblioteca: Error");
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
        return "El libro con ese ISBN no está en la biblioteca";
    }
	
	//Método para buscar por título
	public String buscaTitulo(String titulo){
        Libro libro;           
        for(int i=0; i<biblioteca.size(); i++){
            libro=biblioteca.get(i);
            
            if(libro.getTitulo().equals(titulo)){
                return libro.toString();                
            }
        }        
        return "El libro con ese título no está en la biblioteca";
    }
	
	//Método para buscar por autor
	public String buscaAutor(String autor){
		Libro libro = new Libro();
		String resultado = "";
		   for(int i=0; i<biblioteca.size(); i++){
			   libro=biblioteca.get(i);
		            
		       if (libro.getAutor().equals(autor)) {
		    	   resultado+= libro.toString() + "  ,  ";
		    	   if (i == (biblioteca.size() - 1)) {
		    		   return resultado;
		            }
		        }
		     } 
	        return "No hay libros de ese autor en la biblioteca";
	    }
						
	//Método para mostrar al lista de libros de la biblioteca.            	      
	public String mostrarBiblioteca() {
		String resultado="";
		if (biblioteca.size()==0) {
			System.out.println("la lista está vacia");
		}else {
			for (Libro var: biblioteca)
				resultado+= var.toString() + " ---";
		}
			return resultado;	
	}
	
	//Método para mostrar el último libro añadido a la biblioteca. 
	public String mostrarUltimoBiblioteca() {
		String resultado="";
		if (biblioteca.size()==0) {
			System.out.println("la lista está vacia");
		}else {
			resultado=biblioteca.get(biblioteca.size()-1).toString();
		}
		return resultado;	
	}
	
		//Método para agregar libros a la bilioteca.
		public boolean addLibro(Libro libro) {
			return biblioteca.add(libro);
		}	
		
		
		public ArrayList<Libro> getBiblioteca() {
			return biblioteca;
		}

		public void setBiblioteca(ArrayList<Libro> biblioteca) {
			this.biblioteca = biblioteca;
		}


		public static void main(String[] args) {
//			
//		HiloBiblioteca hilosss = new HiloBiblioteca();
//		String salida = hilosss.buscaAutor("Patrick Rothfuss");
//		hilosss.mostrarBiblioteca();
			
//			String libros[]=new String[3];
//			libros[0]="gfdgfdhbgfd";
//			libros[1]="iiiiiiiiiii";
//			libros[2]="yhhdhhhhhh";
//			
//			 for (int i=0; i<libros.length; i++) {
//				 System.out.println(libros[i]);	
//		Scanner sc= new Scanner(System.in);
//		
//		System.out.println("introduce un ISBN");
//		String textos = sc.nextLine() + ("%");
//		System.out.println("introduce un autor");
//		textos += sc.nextLine() + ("%");
//		
//		System.out.println(textos);
		
//		sc.close();
			
//		HiloBiblioteca hilo1= new HiloBiblioteca();
//		Libro libro1= new Libro("666", "caca", "culo", 22);
//		hilo1.addLibro(libro1);
//		System.out.println(hilo1.mostrarUltimoBiblioteca());
//		
		
		
			 }
			
			
		}
	
	


