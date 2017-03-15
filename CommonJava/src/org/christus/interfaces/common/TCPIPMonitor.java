package org.christus.interfaces.common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPIPMonitor {
	public  boolean isPortLive(String adress, int port){
		try {
			InetAddress inetAddress = InetAddress.getByName(adress); 
			Socket s = new Socket(inetAddress,port);
			s.close();
			return true;
		} catch (IOException e) {
			return false;  
		}
	}
	
	public static String isPortActive(String adress, String port){
		try {
			InetAddress inetAddress = InetAddress.getByName(adress); 
			Socket s = new Socket(inetAddress,Integer.parseInt(port));
			s.close();
			return "ACTIVE";
		} catch (IOException e) {
			return "INACTIVE";  
		}
	}
	public static String  processActivation(String host, String port, String queueMgrName, String queueName) throws InterruptedException{
		
		boolean ipLive = false;
		String strReturn = "INACTIVE";
		TCPIPMonitor ipMonitor = new TCPIPMonitor();
		for (;!ipLive;){
			
			ipLive = ipMonitor.isPortLive(host,Integer.parseInt(port));
			if(ipLive){
				strReturn = "ACTIVE";
				AdminQueue.queueEnable(queueMgrName, queueName);
				break;
			}
			Thread.sleep(60000);
			
		}
		return strReturn;
	}

}
