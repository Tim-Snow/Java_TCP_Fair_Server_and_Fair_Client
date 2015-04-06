package common;

import java.util.Hashtable;
import java.util.Map.Entry;

public class Ping {
	private static volatile Hashtable<String, Double> threadPings;
	private static  Hashtable<String, Boolean> threadDoneFlags;
	private static  Hashtable<String, Boolean> threadDoneResetFlags;
	private static  long 		pingTimer 		= 0;
	private static  double highestPing 	= 0;

	public Ping(){
		threadPings					= new Hashtable<String, Double>();
		threadDoneFlags			= new Hashtable<String, Boolean>();	//Flag that all clients pinged and highest ping can be checked/sent
		threadDoneResetFlags	= new Hashtable<String, Boolean>();	//Reset flags for ^
	}
	
	public static void newClient(String id, double time){
		threadPings.put(id, time);
		threadDoneFlags.put(id, false);
		threadDoneResetFlags.put(id, false);
	}

	public static void setPingTime(String id, double time){
		threadPings.put(id, time);	
		threadDoneFlags.put(id, true);
	}

	public static void setTimer(long l){
		pingTimer = l;
	}
	
	public static double getHighestPing(){
		highestPing = 0.0;
		double tmp = 0.0;
		
		for(Entry<String, Double> ent : threadPings.entrySet()){
			if(ent.getValue() != null) tmp =ent.getValue();
			if(tmp > highestPing) highestPing = tmp;
		}
		
		return highestPing;
	}
	
	public static long getTimer(){
		return pingTimer;
	}
	
	public static double getPing(String id){
		return threadPings.get(id);
	}
	
	public static boolean getPingFlag(String id){
		return threadDoneFlags.get(id);
	}

	public static void removeClient(String id){
		threadPings.remove(id);
		threadDoneFlags.remove(id);
		threadDoneResetFlags.remove(id);
	}
	
	public static synchronized boolean canUpdateHPing(String id){
		for(Entry<String, Boolean> ent : threadDoneFlags.entrySet()){
			if(!ent.getValue()) return false;
		}
		
		threadDoneResetFlags.put(id, true);
		boolean flag = true;
		
		for(Entry<String, Boolean> ent : threadDoneResetFlags.entrySet()){
			if(!ent.getValue()) flag = false;
		}
		
		if(flag){ //all threads ready to reset
			for(Entry<String, Boolean> ent : threadDoneFlags.entrySet()){
				ent.setValue(false);
			}
			for(Entry<String, Boolean> ent : threadDoneResetFlags.entrySet()){
				ent.setValue(false);
			}
		}
		
		return true;
	}
}
