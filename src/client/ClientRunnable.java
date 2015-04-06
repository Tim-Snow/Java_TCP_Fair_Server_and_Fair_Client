package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import common.NetStringReader;
import common.NetStringWriter;
import common.NetTCPReader;
import common.NetTCPWriter;

class ClientRunnable implements Runnable {
	private NetStringWriter 		out; 
	private NetStringReader		in;
	private Socket						socket;
	private double 					ping, highestPing, adjustment;
	private String 						message;
	private boolean					replying;

	ClientRunnable(Socket s) {
		highestPing 	= 0.0;
		socket 			= s;
		replying			= false;

		try {
			out 	= new NetTCPWriter( socket );
			in  	= new NetTCPReader( socket );
		} catch (IOException e) { e.printStackTrace(); }
	}

	private void calcPing(){
		System.out.println("Waiting for server..");

		in.get();   
		out.put("p");

		System.out.println("Server response recieved.");
		String 	pingTime 	= in.get();
		Scanner sc 				= new Scanner(pingTime);
		ping							= sc.nextDouble();
		highestPing 				= sc.nextDouble();
		adjustment 				= highestPing - ping;
		sc.close();

		System.out.println("Ping time to server (ms): " + ping);
		System.out.println("Highest Ping : " + highestPing);
		System.out.printf(" Adjustment : %12.9f\n", adjustment);
		out.put("n");
	}
	
	private void updateHighPing(String m){
		Scanner sc = new Scanner(m);
		sc.next();

		highestPing 	= sc.nextDouble();
		adjustment 	= highestPing - ping;
		System.out.printf("Highest ping UPDATED : %12.9f\n", highestPing);
		System.out.printf(" Adjustment : %12.9f\n", adjustment);
		out.put("n");
		sc.close();
	}

	private void updatePing(String m){
		Scanner sc = new Scanner(m);
		sc.next();

		ping 				= sc.nextDouble();
		System.out.printf("Ping UPDATED : %12.9f\n", ping);
		out.put("n");
		sc.close();
	}

	private void reply(){
		long start 				= System.nanoTime();

		while(true){							
			replying 			 	= true;
			long current  	= System.nanoTime();
			double tt 		 	=  (double) (current - start) / 1_000_000_000;

			if(tt >adjustment)		break;		//Only reply once enough time has passed
		}

		out.put("o");
		replying 					= false;
	}

	@Override
	public void run() {
		calcPing();

		while((message = in.get()) != null) {			
			if(message.equals("o") && !replying)			reply();
			else if(message.startsWith("hu:"))					updateHighPing(message);
			else if(message.startsWith("pu:"))					updatePing(message);
			else 																	out.put("n");
		}

		System.out.println("Lost Connection with Server");

		in.close();		out.close();
		try{					socket.close();
		} catch (IOException e) { e.printStackTrace();	}
	}
}