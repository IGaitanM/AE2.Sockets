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
		hilo.start();
	}
	
	public HiloBiblioteca () {
		biblioteca= new ArrayList<Libro>();
		biblioteca.add(new Libro("9780756413712", "El Nombre Del Viento", "Patrick Rothfuss", 10.40));
		biblioteca.add(new Libro("9788448037246", "El Elfo Oscuro", "R.A Slvatore", 190));
		biblioteca.add(new Libro("9788408043645", "La sombra del viento", "Carlos Ruiz Zafón", 8));
		biblioteca.add(new Libro("9788496940000", "El Médico", "Noah Gordon", 11.35));
		biblioteca.add(new Libro("9788416858217", "Materia OScura", "Blake Crouch", 16.15));
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
				
				if (texto.contains("@")) {
					
					HiloBiblioteca hiloBusca = new HiloBiblioteca();
					texto = texto.replace("@", "");
					respuestaServidor= hiloBusca.buscaIsbn(texto.trim().toString());
					
					System.out.println(hilo.getName() + " busca el libro: " + respuestaServidor );
					
					//Le mandamos la respuesta al cliente
					salida.println(respuestaServidor);
					
					
				} else if (texto.contains("/"))  {
					
					HiloBiblioteca hiloBusca = new HiloBiblioteca();
					texto = texto.replace("/", "").trim();
					respuestaServidor= hiloBusca.buscaTitulo(texto.trim().toString());
					
					System.out.println(hilo.getName() + " busca el libro: " + respuestaServidor );
					
					//Le mandamos la respuesta al cliente
					salida.println(respuestaServidor);
					
				}
					
				else if (texto.contains("*"))  {
						
					HiloBiblioteca hiloBusca = new HiloBiblioteca();
					texto = texto.replace("*", "").trim();
					respuestaServidor= hiloBusca.buscaAutor(texto.toString());
						
					System.out.println(hilo.getName() + " busca el libro: " + respuestaServidor);
						
					//Le mandamos la respuesta al cliente
					salida.println(respuestaServidor);	
					
				} else {
					System.out.println("Servidor cerrado");
					socketAlCliente.close();
					
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
	
	public String buscaAutor(String autor){
		Libro libro;
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
						
		            	      
		public String buscaAutor2(String autor){
			Libro libro=new Libro();
			int i = 0;
			String resultado = null;
			do  {
				for( i=0; i<biblioteca.size(); i++){
		            libro=biblioteca.get(i);
		            
		            if(libro.getTitulo().equals(autor)){
		            	resultado=libro.toString();
		                return resultado;                
		            }
				}
				
				}while (i==biblioteca.size());
			
			return resultado;
					
			
			
		}
	
		
		
		public ArrayList<Libro> getBiblioteca() {
			return biblioteca;
		}

		public void setBiblioteca(ArrayList<Libro> biblioteca) {
			this.biblioteca = biblioteca;
		}

		public static void main(String[] args) {
			
//		HiloBiblioteca hilosss = new HiloBiblioteca();
//		String salida = hilosss.buscaAutor("Patrick Rothfuss");
//		System.out.println(salida);
			
//			String libros[]=new String[3];
//			libros[0]="gfdgfdhbgfd";
//			libros[1]="iiiiiiiiiii";
//			libros[2]="yhhdhhhhhh";
//			
//			 for (int i=0; i<libros.length; i++) {
//				 System.out.println(libros[i]);				 
			 }
			
			
		}
	
	


