package server;

import java.net.*;
import java.util.concurrent.*;

import common.*;

class Server
{
	static final Ping ping = new Ping();
	public static void main( String args[] )
	{ 
		final int port = 50000;

		( new Server() ).process( port );
	}

	public void process( final int port )	{
		try {
			ExecutorService es 	= Executors.newFixedThreadPool(4);
			System.out.println("Server Started.");
			@SuppressWarnings("resource")
			ServerSocket ss 		= new ServerSocket(port);

			while ( true ) {
				Socket socket    		= ss.accept();
				Runnable process 	= new ProcessRequest(socket);
				es.execute( process );
			}
		}
		catch ( Exception e ) {
			System.out.printf("Server.process(): %s\n", e.getMessage() );
		}
	}
}