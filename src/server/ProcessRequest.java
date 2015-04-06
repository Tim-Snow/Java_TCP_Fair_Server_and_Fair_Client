package server;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import common.NetTCPReader;
import common.NetTCPWriter;
import common.Ping;

class ProcessRequest implements Runnable {
	private NetTCPReader 	in;
	private NetTCPWriter 	out;
	private Socket          		socket;
	private static int      		uniqueNo=1;
	private String          			name;
	private double					thisPing, minTime;
	private DateFormat 		dateFormat;
	private boolean				receiving, canSend;

	public ProcessRequest( Socket ss) {
		receiving 		= false;
		canSend 		= false;
		minTime 		= 0.5;
		socket 			= ss; 
		name 				= "Thread  " + uniqueNo++ + " ";
		dateFormat 	= new SimpleDateFormat("HH:mm:ss");
	}

	public void run() {
		try {
			in  	= new NetTCPReader(socket);
			out 	= new NetTCPWriter(socket);

			pingClient();
			
			Ping.setTimer(System.nanoTime());
			long previous 			= System.nanoTime();
			long current, pingCurrent;

			while ( true ) {
				String message 				= in.get();
				current 								= System.nanoTime();
				pingCurrent						= System.nanoTime();
				Double timePassed 		= ((double) (current - previous) / 1_000_000_000 );
				Double timeSincePing 	= ((double) (pingCurrent - Ping.getTimer()) / 1_000_000_000 );
				
				if ( message == null ) break;

				if(timeSincePing > 2 && !Ping.getPingFlag(name) && !receiving){
					System.out.println(name + "pinging");
					checkPing();	
					canSend = true;
				}					

				if(Ping.canUpdateHPing(name) && canSend){
					canSend = false;
					out.put("hu: " + Ping.getHighestPing());						
					Ping.setTimer(System.nanoTime());
				}
				
				if( timePassed >= minTime && timePassed > (Ping.getHighestPing() - thisPing) && !receiving) {
					receiving 	= true;	out.put("o");
				} else {							out.put("n");
				}

				if(receiving && message.equals("o") ){					
					previous 	= System.nanoTime();
					receiving 	= false;
					System.out.println(name + "reply at: " + dateFormat.format(
							Calendar.getInstance().getTime()) + " ns " + System.nanoTime());
				}
			}

			Ping.removeClient(name);
			System.out.println(name + " ended by client.");
			in.close();		out.close();			socket.close();
		}
		catch ( Exception err ) { }
	}
	
	private void checkPing(){
		long start 		= System.nanoTime();
		
		out.put("p");
		in.get();
		
		long current = System.nanoTime();
		double tt 		=  (double) (current - start) / 1_000_000_000;
		
		synchronized(Ping.class){
			Ping.setPingTime(name, tt);	
			thisPing = Ping.getPing(name);
			out.put("pu: " + Double.toString(thisPing));
		}				
	}
	
	private void pingClient(){
		long start 		= System.nanoTime();

		out.put("p");
		in.get();

		long current = System.nanoTime();
		double tt 		=  (double) (current - start) / 1_000_000_000;
		thisPing 			= tt;

		Ping.newClient(name, tt);	
		out.put(Double.toString(tt) + " " + Ping.getHighestPing()); 	//Send client their ping time + highest
		
		System.out.printf("%s - Response Time : %12.9f\n", name, tt);
	}
}