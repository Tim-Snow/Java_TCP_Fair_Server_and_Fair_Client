package  client;

import java.net.*;
import common.*;

class Client {
	private Socket 					socket;
	private ClientRunnable 		process;

	public static void main( String args[] ) {
		( new Client() ).start(args);  
	}

	public void start(String a[]) {
		System.out.println( "Client" );
		//String h = a[0];

		final String 	host 	= "localhost";
		final int    		port 	= 50000; 

		try {
			socket 	= new Socket( host, port );
			process 	= new ClientRunnable(socket);
			process.run();
		}
		catch ( Exception e ) {
			DEBUG.error("Error:\n%s", e.getMessage() );
		}
	}
}